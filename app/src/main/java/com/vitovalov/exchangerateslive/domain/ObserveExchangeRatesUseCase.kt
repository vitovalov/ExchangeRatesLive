package com.vitovalov.exchangerateslive.domain

import com.vitovalov.exchangerateslive.internal.Config
import com.vitovalov.exchangerateslive.domain.model.ExchangeRatesModel
import com.vitovalov.exchangerateslive.presentation.model.ExchangeRateUiModel
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal

class ObserveExchangeRatesUseCase(private val repository: ExchangeRatesRepository) {

    fun run(): Flowable<List<ExchangeRateUiModel>> =
            repository.observeExchangeRates(Config.START_BASE_CURRENCY)
                    .map { m -> m.mapToUiModel() }
                    .subscribeOn(Schedulers.computation())

    private fun ExchangeRatesModel.mapToUiModel(): List<ExchangeRateUiModel> =
            ratesMap.asSequence()
                    .map { ExchangeRateUiModel(it.key, calculateAmount(this, it.value)) }
                    .plus(ExchangeRateUiModel(Config.START_BASE_CURRENCY, calculateAmount(this, BigDecimal("1.0")))).sortedBy { it.currency.currencyCode }
                    .toList()

    private fun calculateAmount(exchangeRatesModel: ExchangeRatesModel, destinationCurrencyRate: BigDecimal):
            BigDecimal = BigDecimal("1.0").multiply(destinationCurrencyRate)

}