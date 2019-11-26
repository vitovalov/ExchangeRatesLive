package com.vitovalov.exchangerateslive.internal.di

import android.app.Application
import com.vitovalov.exchangerateslive.ExchangeRatesLiveApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    ActivityModule::class,
    ExchangeRatesListViewModelModule::class
])
interface ApplicationComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun withApplicationContext(application: Application): Builder

        fun build(): ApplicationComponent
    }

    fun inject(exchangeRatesLiveApp: ExchangeRatesLiveApp)
}