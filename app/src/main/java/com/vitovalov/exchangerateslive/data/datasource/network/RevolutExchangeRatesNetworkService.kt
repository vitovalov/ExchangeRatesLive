package com.vitovalov.exchangerateslive.data.datasource.network

import com.vitovalov.exchangerateslive.domain.model.ExchangeRatesModel
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.util.*

class RevolutExchangeRatesNetworkService(private val apiService: RevolutExchangeRatesApiService) :
    ExchangeRatesNetworkService {

    override fun getExchangeRatesForBase(baseCurrency: Currency): Single<ExchangeRatesModel> =
        apiService.getRatesForBase(baseCurrency)
            .map {
                ExchangeRatesModel(
                    Currency.getInstance(it.baseCurrency),
                    it.ratesMap.map { ratesMap ->
                        Currency.getInstance(ratesMap.key) to BigDecimal(ratesMap.value)
                    }.toMap()
                )
            }.subscribeOn(Schedulers.io())
}