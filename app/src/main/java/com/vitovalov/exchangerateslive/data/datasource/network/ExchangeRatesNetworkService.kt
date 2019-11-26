package com.vitovalov.exchangerateslive.data.datasource.network

import com.vitovalov.exchangerateslive.domain.model.ExchangeRatesModel
import io.reactivex.Single
import java.util.*

interface ExchangeRatesNetworkService {

    fun getExchangeRatesForBase(baseCurrency: Currency): Single<ExchangeRatesModel>
}
