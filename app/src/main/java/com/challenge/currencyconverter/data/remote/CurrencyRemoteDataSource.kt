package com.challenge.currencyconverter.data.remote

import com.challenge.currencyconverter.BuildConfig
import javax.inject.Inject

class CurrencyRemoteDataSource @Inject constructor(
    private val currencyService: CurrencyService
) : BaseDataSource() {
    suspend fun getCurrencies() = getResult {
        currencyService.getAllCurrencies()
    }

    suspend fun getLatestRates(base: String, symbols: String) =
        getResult {
            currencyService.getLatestRates(
                BuildConfig.API_KEY,
                base,
                symbols
            )
        }
}