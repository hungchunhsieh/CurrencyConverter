package com.challenge.currencyconverter.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.challenge.currencyconverter.data.entities.ExchangeRate

@Dao
interface ExchangeRateDao {
    @Query("SELECT * FROM exchange_rate ORDER BY timestamp DESC LIMIT 1")
    fun getExchangeRateLiveData(): LiveData<ExchangeRate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(exchangeRate: ExchangeRate)
}