package com.challenge.currencyconverter.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromString(value: String?): Map<String, Double>? {
        if (value == null) {
            return null
        }
        val mapType = object : TypeToken<Map<String, Double>>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun fromMap(map: Map<String, Double>?): String? {
        if (map == null) {
            return null
        }
        return Gson().toJson(map)
    }
}