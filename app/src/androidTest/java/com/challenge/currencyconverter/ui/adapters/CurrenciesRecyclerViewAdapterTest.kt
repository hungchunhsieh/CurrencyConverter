package com.challenge.currencyconverter.ui.adapters

import android.view.LayoutInflater
import androidx.test.core.app.ApplicationProvider
import com.challenge.currencyconverter.MainApplication
import com.challenge.currencyconverter.databinding.ItemCurrencyBinding
import org.junit.Test
import org.mockito.Mockito.eq

class CurrenciesRecyclerViewAdapterTest {

    @Test
    fun testSetItems() {
        // Given
        val adapter = CurrenciesRecyclerViewAdapter()
        val context = ApplicationProvider.getApplicationContext<MainApplication>()
        val realViewHolder = CurrencyViewHolder(
            ItemCurrencyBinding.inflate(
                LayoutInflater.from(context),
                null,
                false
            )
        )

        // When
        val items = mapOf("USD" to 1.0, "EUR" to 0.8, "GBP" to 0.7)
        adapter.setItems(items)
        adapter.onBindViewHolder(realViewHolder, 0)

        // Expectation
        assert(adapter.itemCount == items.size)
        assert(realViewHolder.itemBinding.name.text.toString() == "USD")
        assert(realViewHolder.itemBinding.exchangeAmount.text.toString() == "0.0")
    }

    @Test
    fun testSetAmount() {
        // Given
        val adapter = CurrenciesRecyclerViewAdapter()
        val context = ApplicationProvider.getApplicationContext<MainApplication>()
        val realViewHolder = CurrencyViewHolder(
            ItemCurrencyBinding.inflate(
                LayoutInflater.from(context),
                null,
                false
            )
        )

        // When
        adapter.setAmount(50.0)

        // Expectation
        realViewHolder.bind("USD", 1.0, eq(50.0))
        realViewHolder.bind("JPY", 148.0, eq(50.0 * 148.0))
    }
}
