package com.challenge.currencyconverter.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rate")
data class ExchangeRate(
    val disclaimer: String,
    val license: String,
    @PrimaryKey
    val timestamp: Int,
    val base: String,
    val rates: Map<String, Double>
)