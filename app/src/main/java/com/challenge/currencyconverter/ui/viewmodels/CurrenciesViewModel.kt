package com.challenge.currencyconverter.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.currencyconverter.data.entities.ExchangeRate
import com.challenge.currencyconverter.data.repository.CurrencyRepository
import com.challenge.currencyconverter.data.repository.ExchangeRateRepository
import com.challenge.currencyconverter.utils.ResourceStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrenciesViewModel @Inject constructor(
    private val repository: CurrencyRepository,
    private val exchangeRateRepository: ExchangeRateRepository,
) : ViewModel() {

    val currencies = repository.getCurrencies()
    val exchangeRates = MutableLiveData<ResourceStatus<ExchangeRate>>()

    fun updateExchangeRates(baseCurrency: String, targetCurrency: String) {
        viewModelScope.launch {
            exchangeRateRepository.getLatestRates(baseCurrency, targetCurrency)
                .observeForever { resourceStatus ->
                    exchangeRates.value = resourceStatus
                }
        }
    }
}
