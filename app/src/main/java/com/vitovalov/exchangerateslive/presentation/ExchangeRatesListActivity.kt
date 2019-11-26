package com.vitovalov.exchangerateslive.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.vitovalov.exchangerateslive.R
import com.vitovalov.exchangerateslive.presentation.model.ExchangeListUiState
import com.vitovalov.exchangerateslive.presentation.model.ExchangeRateUiModel
import dagger.android.AndroidInjection
import io.reactivex.subscribers.DisposableSubscriber
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class ExchangeRatesListActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ExchangeRatesListViewModel
    private lateinit var exchangeRateListAdapter: ExchangeRatesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[ExchangeRatesListViewModel::class.java]

        exchangeRateListAdapter = ExchangeRatesListAdapter(mutableListOf())

        exchange_rates_list.layoutManager = LinearLayoutManager(this@ExchangeRatesListActivity)
        exchange_rates_list.adapter = exchangeRateListAdapter
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
                        Log.e("TODO", "Error: ${t.message}")
                    }
                }
        )
    }

    private fun updateItems(list: List<ExchangeRateUiModel>) {
        Log.d("TODO", "Update: $list")
        exchange_rates_list.visibility = View.VISIBLE
        exchangeRateListAdapter.updateItems(list)

    }

    private fun showLoading() {
        Log.d("TODO", "Loading")
    }

    private fun showError(msg: String) {
        Log.e("TODO", "Error $msg")
    }
}
