package com.vitovalov.exchangerateslive.domain

import com.vitovalov.exchangerateslive.data.local.model.UserChangesModel
import com.vitovalov.exchangerateslive.domain.model.ExchangeRatesModel
import com.vitovalov.exchangerateslive.internal.Config
import com.vitovalov.exchangerateslive.presentation.model.ExchangeRatesUiModel
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.math.MathContext

class ObserveExchangeRatesUseCase(
    private val repository: ExchangeRatesRepository,
    private val mathContext: MathContext
) {

    fun execute(): Flowable<List<ExchangeRatesUiModel>> =
        Flowable.combineLatest(
            repository.observeExchangeRates(Config.START_BASE_CURRENCY),
            repository.observeUserCurrencySelection(),
            BiFunction<ExchangeRatesModel, UserChangesModel,
                    List<ExchangeRatesUiModel>> { currencyExchangeRatesModel, userCurrencySelectionModel ->
                currencyExchangeRatesModel.mapToUiModel(
                    userCurrencySelectionModel
                ).andPlaceBaseCurrencyAtTheTop(userCurrencySelectionModel)
            }).subscribeOn(Schedulers.computation())

    private fun ExchangeRatesModel.mapToUiModel(
        userCurrencySelectionModel: UserChangesModel
    ): List<ExchangeRatesUiModel> = ratesMap.asSequence().map {
        ExchangeRatesUiModel(
            it.key, calculateAmount(this, userCurrencySelectionModel, it.value)
        )
    }.plus(
        ExchangeRatesUiModel(
            Config.START_BASE_CURRENCY, calculateAmount(
                this, userCurrencySelectionModel, BigDecimal("1.0")
            )
        )
    ).sortedBy { it.currency.currencyCode }.toList()

    private fun List<ExchangeRatesUiModel>.andPlaceBaseCurrencyAtTheTop(
        userCurrencySelectionModel: UserChangesModel
    ): List<ExchangeRatesUiModel> =
        sortedByDescending { it.currency == userCurrencySelectionModel.baseCurrency }

    private fun calculateAmount(
        currencyExchangeRatesModel: ExchangeRatesModel,
        userCurrencySelectionModel: UserChangesModel,
        destinationCurrencyRate: BigDecimal
    ): BigDecimal =

        when (userCurrencySelectionModel.baseCurrency == Config.START_BASE_CURRENCY) {
            true -> userCurrencySelectionModel.currencyAmount.multiply(
                destinationCurrencyRate
            )
            false -> userCurrencySelectionModel.currencyAmount.divide(
                currencyExchangeRatesModel.ratesMap[userCurrencySelectionModel.baseCurrency],
                mathContext
            ).multiply(destinationCurrencyRate, mathContext)
        }
}