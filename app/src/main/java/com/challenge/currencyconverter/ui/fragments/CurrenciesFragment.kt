package com.challenge.currencyconverter.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.challenge.currencyconverter.R
import com.challenge.currencyconverter.data.entities.Currency
import com.challenge.currencyconverter.databinding.CurrenciesFragmentBinding
import com.challenge.currencyconverter.ui.adapters.AutoCompleteCurrencyAdapter
import com.challenge.currencyconverter.ui.adapters.CurrenciesRecyclerViewAdapter
import com.challenge.currencyconverter.ui.viewmodels.CurrenciesViewModel
import com.challenge.currencyconverter.utils.ResourceStatus
import com.challenge.currencyconverter.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CurrenciesFragment : Fragment() {

    private var binding: CurrenciesFragmentBinding by autoCleared()
    private val viewModel: CurrenciesViewModel by viewModels()
    private lateinit var currenciesRecyclerViewAdapter: CurrenciesRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CurrenciesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAmountInputEditor()
        setupRecyclerView()
        setupObservers()
    }

    private fun setupAmountInputEditor() {
        binding.numberInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // Do nothing before the text changes
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                // Do nothing during the text changing
            }

            override fun afterTextChanged(editable: Editable?) {
                if (editable != null) {
                    var amount = editable.toString()
                    amount = amount.ifEmpty { "0.00" }
                    currenciesRecyclerViewAdapter.setAmount(amount.toDouble())
                }
            }
        })
    }

    private fun setupRecyclerView() {
        currenciesRecyclerViewAdapter = CurrenciesRecyclerViewAdapter()
        binding.currenciesRv.layoutManager = LinearLayoutManager(requireContext())
        binding.currenciesRv.adapter = currenciesRecyclerViewAdapter
    }

    private fun setupObservers() {
        viewModel.currencies.observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                ResourceStatus.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE

                    if (!it.data.isNullOrEmpty()) {
                        val currencyCodeList = it.data.mapTo(ArrayList()) { it.key }
                        val autoCompleteCurrencyAdapter = AutoCompleteCurrencyAdapter(
                            requireContext(),
                            R.layout.list_currency,
                            currencyCodeList,
                            it.data
                        )
                        binding.autoCompleteTextview.setAdapter(autoCompleteCurrencyAdapter)
                        setOnAutoCompleteChangeListener(
                            binding.autoCompleteTextview,
                            currencyCodeList,
                            it.data
                        )

                        /* Set default value to USD:
                        Changing the API `base` currency is available for
                        Developer, Enterprise and Unlimited plan clients,
                        for Free account only allow to use "USD" as base currency. */

                        val initCurrency = "USD"
                        viewModel.updateExchangeRates(
                            initCurrency,
                            currencyCodeList.joinToString(",")
                        )
                        binding.autoCompleteTextview.setText(initCurrency, false)
                    }
                }

                ResourceStatus.Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }

                ResourceStatus.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })

        viewModel.exchangeRates.observe(viewLifecycleOwner, Observer { it ->
            it?.let {
                when (it.status) {
                    ResourceStatus.Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        Log.d("CurrenciesFragment", it.toString())

                        if (it.data != null) {
                            currenciesRecyclerViewAdapter.setItems(it.data.rates)
                            var amount = binding.numberInput.text.toString()
                            amount = amount.ifEmpty { "0.00" }
                            currenciesRecyclerViewAdapter.setAmount(amount.toDouble())
                        }
                    }

                    ResourceStatus.Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    ResourceStatus.Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun setOnAutoCompleteChangeListener(
        autoCompleteTextView: AutoCompleteTextView,
        currencyCodeList: ArrayList<String>,
        data: List<Currency>
    ) {
        autoCompleteTextView.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                val selectedItem = data[position]
                if (selectedItem.key != null && currencyCodeList.size > 0) {
                    viewModel.updateExchangeRates(
                        selectedItem.key,
                        currencyCodeList.joinToString(",")
                    )
                }
            }
    }
}