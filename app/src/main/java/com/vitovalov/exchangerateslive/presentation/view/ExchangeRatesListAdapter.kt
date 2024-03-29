package com.vitovalov.exchangerateslive.presentation.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vitovalov.exchangerateslive.R
import com.vitovalov.exchangerateslive.presentation.model.ExchangeRatesUiModel
import com.vitovalov.exchangerateslive.presentation.view.uiutils.AmountInputTextWatcher
import com.vitovalov.exchangerateslive.presentation.view.uiutils.CurrencyAmountDifference
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.adapter_item_rate_info.view.*
import java.math.BigDecimal

class ExchangeRatesListAdapter(
    private val onCurrencySelected: (ExchangeRatesUiModel) -> Unit,
    onAmountChanged: (BigDecimal) -> Unit,
    private val onCalculateDiffList: (List<ExchangeRatesUiModel>, List<ExchangeRatesUiModel>) -> Single<DiffUtil.DiffResult>,
    private val loadImage: (ImageView, ExchangeRatesUiModel) -> Unit,
    private val itemList: MutableList<ExchangeRatesUiModel>
) : RecyclerView.Adapter<ExchangeRatesListAdapter.RateInfoViewHolder>() {

    private val amountInputTextWatcher = AmountInputTextWatcher(onAmountChanged)
    private var listsDiffDisposable: Disposable? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateInfoViewHolder {
        val viewHolder =
            RateInfoViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.adapter_item_rate_info, parent, false
                )
            )
        viewHolder.itemView.setOnClickListener {
            if (viewHolder.adapterPosition != 0) viewHolder.currencyAmountEditText.requestFocus()
        }

        viewHolder.currencyAmountEditText.apply {
            setOnFocusChangeListener { _, focused ->
                when (focused) {
                    true -> {
                        onCurrencySelected(itemList[viewHolder.adapterPosition])
                        addTextChangedListener(amountInputTextWatcher)
                    }
                    false -> removeTextChangedListener(amountInputTextWatcher)
                }
            }
        }
        return viewHolder
    }

    override fun getItemCount() = itemList.size

    override fun getItemId(position: Int): Long = itemList[position].currency.hashCode().toLong()

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        listsDiffDisposable?.dispose()
    }

    override fun onBindViewHolder(holder: RateInfoViewHolder, position: Int) {
        val currencyItem = itemList[position]
        holder.currencyCodeText.text = currencyItem.currency.currencyCode
        holder.currencyNameText.text = currencyItem.currency.displayName
        loadImage(holder.currencyFlagImage, currencyItem)
        with(holder.currencyAmountEditText) {
            if (!isFocused) setText(currencyItem.amount.toPlainString())
        }
    }

    override fun onBindViewHolder(
        holder: RateInfoViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        when (payloads.isEmpty()) {
            true -> onBindViewHolder(holder, position) // full bind
            // partial bind
            false -> with(holder.currencyAmountEditText) {
                if (!isFocused) { // if not first item: the one that user updates
                    setText((payloads[0] as CurrencyAmountDifference).newCurrencyAmountValue.toPlainString())
                }
            }
        }
    }

    fun updateItems(newList: List<ExchangeRatesUiModel>) {
        listsDiffDisposable?.dispose()
        onCalculateDiffList(itemList, newList)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                itemList.clear()
                itemList.addAll(newList)
                it.dispatchUpdatesTo(this@ExchangeRatesListAdapter)
            }, {})
            .let { listsDiffDisposable = it }
    }

    class RateInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val currencyCodeText: TextView = itemView.currency_code_tv
        val currencyNameText: TextView = itemView.currency_fullname_tv
        val currencyAmountEditText: EditText = itemView.amount_tv
        val currencyFlagImage: ImageView = itemView.currency_flag
    }
}
