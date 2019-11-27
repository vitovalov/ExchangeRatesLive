package com.vitovalov.exchangerateslive.domain

import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal

class UpdateUserAmountUseCase(private val repository: ExchangeRatesRepository) {

    fun execute(newAmount: BigDecimal): Completable =
            repository
                    .updateUserAmount(newAmount)
                    .subscribeOn(Schedulers.io())
}