package com.vitovalov.exchangerateslive.data.network

import android.content.Context
import android.util.Log
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.vitovalov.exchangerateslive.domain.model.ExchangeRatesModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.util.*

class ExchangeRatesRevoNetworkDatasource(
    private val apiService: ExchangeRatesApiService,
    val context: Context
) :
    ExchangeRatesNetworkDatasource {

    override fun getExchangeRatesForBase(baseCurrency: Currency): Single<ExchangeRatesModel> {


        return apiService.getRatesForBase(baseCurrency)
            .map {
                ExchangeRatesModel(
                    Currency.getInstance(it.baseCurrency),
                    it.ratesMap.map { ratesMap ->
                        Currency.getInstance(ratesMap.key) to BigDecimal(ratesMap.value)
                    }.toMap()
                )
            }.subscribeOn(Schedulers.io())
    }
}