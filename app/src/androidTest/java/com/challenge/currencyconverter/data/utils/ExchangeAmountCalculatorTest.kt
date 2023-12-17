package com.challenge.currencyconverter.data.utils

import com.challenge.currencyconverter.utils.calculateExchangeAmount
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class ExchangeAmountCalculatorTest {

    @Test
    fun testCalculateExchangeAmount() {
        // Valid input
        assertEquals(50.0, calculateExchangeAmount(10.0, 5.0), 0.01)

        // Ensure that negative originalAmount throws an exception
        val exception1 = assertThrows(IllegalArgumentException::class.java) {
            calculateExchangeAmount(-10.0, 5.0)
        }
        assertEquals("Amount and exchange rate must be positive values.", exception1.message)

        // Ensure that zero exchangeRate throws an exception
        val exception2 = assertThrows(IllegalArgumentException::class.java) {
            calculateExchangeAmount(10.0, 0.0)
        }
        assertEquals("Amount and exchange rate must be positive values.", exception2.message)
    }
}
