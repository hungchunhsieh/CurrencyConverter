package com.challenge.currencyconverter.ui.adapters

import android.content.Context
import android.widget.ListView
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import com.challenge.currencyconverter.MainApplication
import com.challenge.currencyconverter.R
import com.challenge.currencyconverter.data.entities.Currency
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test


class AutoCompleteCurrencyAdapterTest {

    private lateinit var context: Context
    private lateinit var adapter: AutoCompleteCurrencyAdapter

    // Example data for testing
    private val currencyNames = listOf("USD", "EUR", "GBP")

    private val currencies = listOf(
        Currency("USD", "US Dollar"),
        Currency("EUR", "Euro"),
        Currency("GBP", "British Pound")
    )

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext<MainApplication>()
        adapter =
            AutoCompleteCurrencyAdapter(context, R.layout.list_currency, currencyNames, currencies)
    }

    @Test
    fun testGetView() {
        val parent = ListView(context)

        // Test with the first item in the adapter
        val position = 0
        val view = adapter.getView(position, null, parent)

        assertNotNull(view)

        // Verify that TextViews are populated correctly
        val currencyCodeView = view.findViewById<TextView>(R.id.list_currency_code)
        val currencyNameView = view.findViewById<TextView>(R.id.list_currency_name)

        assertEquals("USD", currencyCodeView.text.toString())
        assertEquals("US Dollar", currencyNameView.text.toString())
    }

    @Test
    fun testGetItemCount() {
        assertEquals(adapter.count, currencies.count())
    }
}