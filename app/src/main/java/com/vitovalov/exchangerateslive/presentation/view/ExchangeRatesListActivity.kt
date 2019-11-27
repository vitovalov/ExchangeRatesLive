package com.vitovalov.exchangerateslive.presentation.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.vitovalov.exchangerateslive.R
import com.vitovalov.exchangerateslive.presentation.model.ExchangeListUiState
import com.vitovalov.exchangerateslive.presentation.model.ExchangeRatesUiModel
import dagger.android.AndroidInjection
import io.reactivex.subscribers.DisposableSubscriber
import kotlinx.android.synthetic.main.activity_exchange_rates_list.*
import javax.inject.Inject

class ExchangeRatesListActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ExchangeRatesListViewModel
    private lateinit var exchangeRateListAdapter: ExchangeRatesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange_rates_list)

        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[ExchangeRatesListViewModel::class.java]

        exchangeRateListAdapter =
            ExchangeRatesListAdapter(
                viewModel::onCurrencySelected,
                viewModel::onAmountChanged,
                viewModel::calculateListsDifferences,
                viewModel::loadItemImage,
                mutableListOf()
            ).apply { setHasStableIds(true) }

        exchange_rates_list.apply {
            layoutManager = LinearLayoutManager(this@ExchangeRatesListActivity)
            adapter = exchangeRateListAdapter
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.observeExchangeListState(
            object : DisposableSubscriber<ExchangeListUiState>() {
                override fun onComplete() {}

                override fun onNext(state: ExchangeListUiState) {
                    when (state) {
                        is ExchangeListUiState.Loading -> showLoading()
                        is ExchangeListUiState.Error -> showError(getString(R.string.generic_error))
                        is ExchangeListUiState.Update -> updateItems(state.currencyExchangeList)
                    }
                }

                override fun onError(t: Throwable) {
                    showError(getString(R.string.generic_error))
                }
            }
        )
    }

    private fun updateItems(list: List<ExchangeRatesUiModel>) {
        exchange_rates_list.visibility = View.VISIBLE
        exchangeRateListAdapter.updateItems(list)

        error_image.visibility = View.GONE
        error_text.visibility = View.GONE
        progress_bar.visibility = View.GONE
        progress_text.visibility = View.GONE
    }

    private fun showLoading() {
        progress_bar.visibility = View.VISIBLE

        progress_text.visibility = View.GONE
        error_image.visibility = View.GONE
        error_text.visibility = View.GONE
        exchange_rates_list.visibility = View.GONE
    }

    private fun showError(message: String) {
        error_text.text = message

        error_image.visibility = View.VISIBLE
        error_text.visibility = View.VISIBLE

        progress_bar.visibility = View.GONE
        progress_text.visibility = View.GONE
        exchange_rates_list.visibility = View.GONE
    }
}
