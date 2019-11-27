package com.vitovalov.exchangerateslive.internal.di

import com.vitovalov.exchangerateslive.data.ExchangeRatesDatasource
import com.vitovalov.exchangerateslive.data.local.UserChangesLocalDatasource
import com.vitovalov.exchangerateslive.data.network.ExchangeRatesNetworkDatasource
import com.vitovalov.exchangerateslive.domain.ExchangeRatesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun providesCurrencyExchangeRatesRepository(
        remoteDatasource: ExchangeRatesNetworkDatasource,
        localDatasource: UserChangesLocalDatasource
    ): ExchangeRatesRepository =
        ExchangeRatesDatasource(
            remoteDatasource,
            localDatasource
        )
}