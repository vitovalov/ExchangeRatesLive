package com.vitovalov.exchangerateslive.domain

import com.vitovalov.exchangerateslive.domain.model.ExchangeRatesModel
import io.reactivex.Flowable
import java.util.*

interface ExchangeRatesRepository{
    fun observeExchangeRates(baseCurrency: Currency): Flowable<ExchangeRatesModel>

}