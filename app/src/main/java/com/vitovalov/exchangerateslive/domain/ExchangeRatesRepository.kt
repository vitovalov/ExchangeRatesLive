package com.vitovalov.exchangerateslive.domain

import com.vitovalov.exchangerateslive.data.datasource.local.model.UserChangesModel
import com.vitovalov.exchangerateslive.domain.model.ExchangeRatesModel
import io.reactivex.Completable
import io.reactivex.Flowable
import java.math.BigDecimal
import java.util.*

interface ExchangeRatesRepository{

    fun observeExchangeRates(baseCurrency: Currency): Flowable<ExchangeRatesModel>
    fun updateUserAmount(newAmount: BigDecimal): Completable
    fun updateUserBaseCurrency(baseCurrency: Currency): Completable
    fun observeUserCurrencySelection(): Flowable<UserChangesModel>

}