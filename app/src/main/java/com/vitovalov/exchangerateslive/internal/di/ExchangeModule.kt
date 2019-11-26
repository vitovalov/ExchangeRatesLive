package com.vitovalov.exchangerateslive.internal.di

import com.vitovalov.exchangerateslive.domain.ExchangeRatesRepository
import com.vitovalov.exchangerateslive.domain.ObserveExchangeRatesUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ExchangeModule {

    @Singleton
    @Provides
    fun providesObserveExchangeRatesUseCase(repository: ExchangeRatesRepository): ObserveExchangeRatesUseCase = ObserveExchangeRatesUseCase(repository)
}