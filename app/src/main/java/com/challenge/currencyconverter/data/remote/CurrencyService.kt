package com.challenge.currencyconverter.data.remote

import com.challenge.currencyconverter.data.entities.ExchangeRate
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {

    @GET("currencies.json")
    suspend fun getAllCurrencies(): Response<Map<String, String>>

    @GET("latest.json")
    suspend fun getLatestRates(
        @Query("app_id") apiId: String,
        @Query("base") base: String,
        @Query("symbols") symbols: String
    ): Response<ExchangeRate>

}