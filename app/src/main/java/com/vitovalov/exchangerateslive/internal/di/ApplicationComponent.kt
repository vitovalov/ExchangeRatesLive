package com.vitovalov.exchangerateslive.internal.di

import android.app.Application
import android.content.Context
import com.vitovalov.exchangerateslive.ExchangeRatesLiveApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    ActivityModule::class,
    NetworkDatasourceModule::class,
    RepositoryModule::class,
    LocalDatasourceModule::class,
    ExchangeModule::class,
    ExchangeRatesListViewModelModule::class
])
interface ApplicationComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun withApplicationContext(context: Context): Builder

        fun build(): ApplicationComponent
    }

    fun inject(exchangeRatesLiveApp: ExchangeRatesLiveApp)
}