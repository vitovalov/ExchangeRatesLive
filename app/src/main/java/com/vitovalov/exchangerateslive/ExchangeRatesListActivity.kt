package com.vitovalov.exchangerateslive

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class ExchangeRatesListActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ExchangeRatesListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[ExchangeRatesListViewModel::class.java]

        testViewModel()
    }

    private fun testViewModel() {
        textView.text = viewModel.testText

        textView.postDelayed({
            viewModel.testText = "newValue=${System.nanoTime()}"
            textView.text = viewModel.testText
        }, 2000)
    }
}
