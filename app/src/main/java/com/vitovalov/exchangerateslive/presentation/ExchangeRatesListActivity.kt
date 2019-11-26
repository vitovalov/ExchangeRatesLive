package com.vitovalov.exchangerateslive.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.vitovalov.exchangerateslive.R
import dagger.android.AndroidInjection
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

        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[ExchangeRatesListViewModel::class.java]

        exchangeRateListAdapter =
            ExchangeRatesListAdapter()

        exchange_rates_item_list.layoutManager = LinearLayoutManager(this@ExchangeRatesListActivity)
        exchange_rates_item_list.adapter = exchangeRateListAdapter
    }
}
