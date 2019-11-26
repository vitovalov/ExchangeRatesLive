package com.vitovalov.exchangerateslive.domain

import com.vitovalov.exchangerateslive.data.datasource.local.model.UserChangesModel
import com.vitovalov.exchangerateslive.domain.model.ExchangeRatesModel
import com.vitovalov.exchangerateslive.internal.Config
import com.vitovalov.exchangerateslive.presentation.model.ExchangeRateUiModel
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.math.MathContext

class ObserveExchangeRatesUseCase(
    private val repository: ExchangeRatesRepository,
    private val mathContext: MathContext
) {

    fun execute(): Flowable<List<ExchangeRateUiModel>> =
        Flowable.combineLatest(
            repository.observeExchangeRates(Config.START_BASE_CURRENCY),
            repository.observeUserCurrencySelection(),
            BiFunction<ExchangeRatesModel, UserChangesModel,
                    List<ExchangeRateUiModel>> { currencyExchangeRatesModel, userCurrencySelectionModel ->
                currencyExchangeRatesModel.mapToUiModel(
                    userCurrencySelectionModel
                ).andPlaceBaseCurrencyAtTheTop(userCurrencySelectionModel)
            }).subscribeOn(Schedulers.computation())

    private fun ExchangeRatesModel.mapToUiModel(
        userCurrencySelectionModel: UserChangesModel
    ): List<ExchangeRateUiModel> = ratesMap.asSequence().map {
        ExchangeRateUiModel(
            it.key, calculateAmount(this, userCurrencySelectionModel, it.value)
        )
    }.plus(
        ExchangeRateUiModel(
            Config.START_BASE_CURRENCY, calculateAmount(
                this, userCurrencySelectionModel, BigDecimal("1.0")
            )
        )
    ).sortedBy { it.currency.currencyCode }.toList()

    private fun List<ExchangeRateUiModel>.andPlaceBaseCurrencyAtTheTop(
        userCurrencySelectionModel: UserChangesModel
    ): List<ExchangeRateUiModel> =
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