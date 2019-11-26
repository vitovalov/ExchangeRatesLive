package com.vitovalov.exchangerateslive.internal.di

import com.vitovalov.exchangerateslive.domain.UpdateBaseCurrencyUseCase
import com.vitovalov.exchangerateslive.domain.ExchangeRatesRepository
import com.vitovalov.exchangerateslive.domain.ObserveExchangeRatesUseCase
import com.vitovalov.exchangerateslive.domain.UpdateUserAmountUseCase
import com.vitovalov.exchangerateslive.internal.Config
import dagger.Module
import dagger.Provides
import java.math.MathContext
import javax.inject.Singleton

@Module
class ExchangeModule {

    @Singleton
    @Provides
    fun providesObserveExchangeRatesUseCase(
        repository: ExchangeRatesRepository,
        mathContext: MathContext
    ): ObserveExchangeRatesUseCase = ObserveExchangeRatesUseCase(repository, mathContext)

    @Singleton
    @Provides
    fun providesUpdateUserAmountUseCase(repository: ExchangeRatesRepository):
            UpdateUserAmountUseCase = UpdateUserAmountUseCase(repository)

    @Singleton
    @Provides
    fun providesUpdateBaseCurrencyUseCase(repository: ExchangeRatesRepository):
            UpdateBaseCurrencyUseCase = UpdateBaseCurrencyUseCase(repository)

    @Singleton
    @Provides
    fun providesMathContext(): MathContext =
        MathContext(Config.MATH_PRECISION, Config.MATH_ROUNDING_MODE)
}