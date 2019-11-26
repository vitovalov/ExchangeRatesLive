package com.vitovalov.exchangerateslive.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vitovalov.exchangerateslive.R

class ExchangeRatesListAdapter :
    RecyclerView.Adapter<ExchangeRatesListAdapter.RateInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateInfoViewHolder {
        val viewHolder =
            RateInfoViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_rate_info,
                    parent,
                    false
                )
            )

        return viewHolder
    }

    override fun getItemCount(): Int {
        return 0
    }

    override fun onBindViewHolder(holder: RateInfoViewHolder, position: Int) {

    }

    class RateInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}
