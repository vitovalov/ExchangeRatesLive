package com.vitovalov.exchangerateslive.data.network

import com.vitovalov.exchangerateslive.domain.model.ExchangeRatesModel
import io.reactivex.Single
import java.util.*

interface ExchangeRatesNetworkDatasource {

    fun getExchangeRatesForBase(baseCurrency: Currency): Single<ExchangeRatesModel>
}
