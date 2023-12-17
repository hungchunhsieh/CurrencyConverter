package com.challenge.currencyconverter.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.challenge.currencyconverter.utils.calculateExchangeAmount
import com.challenge.currencyconverter.databinding.ItemCurrencyBinding

class CurrenciesRecyclerViewAdapter() : RecyclerView.Adapter<CurrencyViewHolder>() {

    private val items: MutableList<Pair<String, Double>> = mutableListOf()
    private var amount: Double = 0.0

    fun setItems(newItems: Map<String, Double>) {
        items.clear()
        items.addAll(newItems.toList())
        notifyDataSetChanged()
    }

    fun setAmount(newAmount: Double) {
        amount = newAmount
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding: ItemCurrencyBinding =
            ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val (key, value) = items[position]
        holder.bind(key, value, amount)
    }
}

class CurrencyViewHolder(
    val itemBinding: ItemCurrencyBinding,
) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(key: String, rate: Double, amount: Double) {
        itemBinding.name.text = key
        var newValue = calculateExchangeAmount(amount, rate)
        itemBinding.exchangeAmount.text = newValue.toString()
    }
}