package com.challenge.currencyconverter.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.challenge.currencyconverter.R
import com.challenge.currencyconverter.data.entities.Currency

class AutoCompleteCurrencyAdapter(
    context: Context,
    resource: Int,
    objects: List<String>,
    items: List<Currency>
) :
    ArrayAdapter<String>(context, resource, objects) {

    private val items = ArrayList<Currency>(items)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        val item = getItem(position)
        if (itemView == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            itemView = inflater.inflate(R.layout.list_currency, parent, false)
        }

        if (itemView != null && item != null) {
            val currencyCodeView = itemView.findViewById<TextView>(R.id.list_currency_code)
            currencyCodeView.text = item

            val currencyNameView = itemView.findViewById<TextView>(R.id.list_currency_name)
            currencyNameView.text = items[position].value
        }

        return itemView!!
    }

}