package com.vitovalov.exchangerateslive.internal.di

import com.vitovalov.exchangerateslive.data.datasource.ExchangeRatesDatasource
import com.vitovalov.exchangerateslive.data.datasource.network.ExchangeRatesNetworkService
import com.vitovalov.exchangerateslive.domain.ExchangeRatesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun providesCurrencyExchangeRatesRepository(
            remoteService: ExchangeRatesNetworkService): ExchangeRatesRepository = ExchangeRatesDatasource(remoteService)
}