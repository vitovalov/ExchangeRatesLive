package com.vitovalov.exchangerateslive.domain

import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import java.util.*

class UpdateBaseCurrencyUseCase(private val repository: ExchangeRatesRepository) {

    fun execute(newBaseCurrency: Currency): Completable =
        repository.updateUserBaseCurrency(newBaseCurrency)
            .subscribeOn(Schedulers.io())
}