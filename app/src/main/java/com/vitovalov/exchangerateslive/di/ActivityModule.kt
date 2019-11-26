package com.vitovalov.exchangerateslive.di

import com.vitovalov.exchangerateslive.ExchangeRatesListActivity
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityModule : AndroidInjector<ExchangeRatesListActivity> {

    @ContributesAndroidInjector
    fun contributeExchangeRatesListActivity(): ExchangeRatesListActivity
}
