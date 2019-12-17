package com.vitovalov.exchangerateslive.domain

import com.vitovalov.exchangerateslive.data.local.model.UserChangesModel
import com.vitovalov.exchangerateslive.domain.model.ExchangeRatesModel
import com.vitovalov.exchangerateslive.presentation.model.ExchangeRatesUiModel
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.math.MathContext

class ObserveExchangeRatesUseCase(
    private val repository: ExchangeRatesRepository,
    private val mathContext: MathContext
) {
    fun execute(): Flowable<List<ExchangeRatesUiModel>> =
        repository.observeUserCurrencySelection().doOnNext {
            println("inside useCase doOnNext")

        }.switchMap {
                userChanges ->
            repository.observeExchangeRates(userChanges.baseCurrency).map { newRates ->
                newRates.mapToUiModel(userChanges).andPlaceBaseCurrencyAtTheTop(userChanges)
            }
        }.subscribeOn(Schedulers.io())


    private fun ExchangeRatesModel.mapToUiModel(
        userCurrencySelectionModel: UserChangesModel
    ): List<ExchangeRatesUiModel> = ratesMap.asSequence().map {
        ExchangeRatesUiModel(
            it.key, calculateAmount(userCurrencySelectionModel, it.value)
        )
    }.plus(
        ExchangeRatesUiModel(
            userCurrencySelectionModel.baseCurrency, calculateAmount(
                userCurrencySelectionModel, BigDecimal("1.0")
            )
        )
    ).sortedBy { it.currency.currencyCode }.toList()

    private fun List<ExchangeRatesUiModel>.andPlaceBaseCurrencyAtTheTop(
        userCurrencySelectionModel: UserChangesModel
    ): List<ExchangeRatesUiModel> =
        sortedByDescending { it.currency == userCurrencySelectionModel.baseCurrency }

    private fun calculateAmount(
        userCurrencySelectionModel: UserChangesModel,
        destinationCurrencyRate: BigDecimal
    ): BigDecimal =
        userCurrencySelectionModel.currencyAmount.multiply(destinationCurrencyRate, mathContext)
}