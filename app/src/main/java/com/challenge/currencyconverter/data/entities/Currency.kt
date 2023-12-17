package com.challenge.currencyconverter.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class Currency(
    @PrimaryKey
    val key: String,
    val value: String,
)