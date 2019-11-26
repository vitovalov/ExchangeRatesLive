package com.vitovalov.exchangerateslive.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vitovalov.exchangerateslive.R
import com.vitovalov.exchangerateslive.presentation.model.ExchangeRateUiModel
import kotlinx.android.synthetic.main.item_rate_info.view.*

class ExchangeRatesListAdapter(
        private var itemList: MutableList<ExchangeRateUiModel>
) :
        RecyclerView.Adapter<ExchangeRatesListAdapter.RateInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateInfoViewHolder {
        val viewHolder =
                RateInfoViewHolder(
                        LayoutInflater.from(parent.context).inflate(
                                R.layout.item_rate_info, parent, false
                        )
                )

        return viewHolder
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: RateInfoViewHolder, position: Int) {
        val currencyItem = itemList[position]
        holder.currencyCodeText?.text = currencyItem.currency.currencyCode
        holder.currencyNameText?.text = currencyItem.currency.displayName
        holder.currencyAmountEditText?.setText(currencyItem.amount.toPlainString())
    }

    fun updateItems(list: List<ExchangeRateUiModel>) {
        itemList.clear()
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    class RateInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val currencyCodeText = itemView.currency_tv
        val currencyNameText = itemView.currency_name_tv
        val currencyAmountEditText = itemView.amount_et
    }
}
