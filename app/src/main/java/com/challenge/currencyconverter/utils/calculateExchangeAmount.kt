package com.challenge.currencyconverter.utils

import kotlin.math.roundToInt

fun calculateExchangeAmount(originalAmount: Double, exchangeRate: Double): Double {
    // Ensure that both originalAmount and exchangeRate are positive values
    if (originalAmount < 0 || exchangeRate <= 0) {
        throw IllegalArgumentException("Amount and exchange rate must be positive values.")
    }

    // Calculate the new amount using the exchange rate
    val newAmount = originalAmount * exchangeRate

    // Round the result to two decimal places (cents)
    return (newAmount * 100.0).roundToInt() / 100.0
}