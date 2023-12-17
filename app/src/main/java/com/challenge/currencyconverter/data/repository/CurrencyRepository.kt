package com.challenge.currencyconverter.data.repository

import com.challenge.currencyconverter.data.entities.Currency
import com.challenge.currencyconverter.data.local.CurrencyDao
import com.challenge.currencyconverter.data.remote.CurrencyRemoteDataSource
import com.challenge.currencyconverter.utils.performGetOperation

import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val remoteDataSource: CurrencyRemoteDataSource,
    private val localDataSource: CurrencyDao
) {

    fun getCurrencies() = performGetOperation(
        databaseQuery = { localDataSource.getAllCurrency() },
        networkCall = { remoteDataSource.getCurrencies() }
    ) { it ->
        val currencies = it.map { Currency(it.key, it.value) } ?: emptyList()
        localDataSource.insertAll(currencies)
    }
}