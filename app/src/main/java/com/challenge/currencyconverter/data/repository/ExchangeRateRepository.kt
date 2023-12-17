package com.challenge.currencyconverter.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.challenge.currencyconverter.BuildConfig
import com.challenge.currencyconverter.data.entities.ExchangeRate
import com.challenge.currencyconverter.data.local.ExchangeRateDao
import com.challenge.currencyconverter.data.remote.CurrencyRemoteDataSource
import com.challenge.currencyconverter.utils.ResourceStatus
import com.challenge.currencyconverter.utils.performGetOperation
import kotlinx.coroutines.flow.first
import java.lang.System.currentTimeMillis

import javax.inject.Inject

class ExchangeRateRepository @Inject constructor(
    private val remoteDataSource: CurrencyRemoteDataSource,
    private val localDataSource: ExchangeRateDao,
    private val dataStore: DataStore<Preferences>
) {

    private val lastSyncTimestampKey = longPreferencesKey("last_sync_timestamp")

    suspend fun getLatestRates(
        baseCurrency: String,
        symbols: String,
    ): LiveData<ResourceStatus<ExchangeRate>> {
        val shouldFetchData = shouldFetchDataFromRemoteDataSource()

        return performGetOperation(
            databaseQuery = {
                if (BuildConfig.IS_FREE_ACCOUNT) {
                    // For Free account using transformation to calculate the new exchange rate
                    localDataSource.getExchangeRateLiveData().map { exchangeRate ->
                        val updatedRates = exchangeRate.rates.mapValues { (_, rateValue) ->
                            // Perform transformations on individual rate values
                            // For example: newRate = baseCurrencyRate / targetRate
                            rateValue.div(exchangeRate.rates[baseCurrency]!!)
                        }

                        // Create a new ExchangeRate object with the updated rates
                        exchangeRate.copy(rates = updatedRates)
                    }
                } else {
                    // If using prime account fetch new exchange rate using API
                    localDataSource.getExchangeRateLiveData()
                }
            },
            networkCall = {
                if (shouldFetchData.value == true) {
                    val base = if (BuildConfig.IS_FREE_ACCOUNT) "USD" else baseCurrency
                    val result = remoteDataSource.getLatestRates(base, symbols)

                    // Save the current timestamp after the network call
                    saveCurrentTimestampToDataStore()

                    result
                } else {
                    // Return a dummy resource indicating that it's loading from the local database
                    ResourceStatus.loading(null)
                }
            }
        ) {
            // Inset latest exchange rate after network call succeed.
            localDataSource.insert(it)
        }
    }

    private suspend fun shouldFetchDataFromRemoteDataSource(): LiveData<Boolean> {
        val shouldFetchData = MutableLiveData<Boolean>()

        val lastSyncTimestamp = dataStore.data.first()[lastSyncTimestampKey] ?: 0L
        val currentTime = currentTimeMillis()
        val thirtyMinutesInMillis = 30 * 60 * 1000 // 30 minutes in milliseconds

        // Check if the last sync timestamp is more than 30 minutes ago
        val isMoreThan30MinutesAgo = currentTime - lastSyncTimestamp > thirtyMinutesInMillis

        // Update the LiveData with the result
        shouldFetchData.postValue(isMoreThan30MinutesAgo)

        return shouldFetchData
    }

    private suspend fun saveCurrentTimestampToDataStore() {
        dataStore.edit { preferences ->
            preferences[lastSyncTimestampKey] = currentTimeMillis()
        }
    }
}