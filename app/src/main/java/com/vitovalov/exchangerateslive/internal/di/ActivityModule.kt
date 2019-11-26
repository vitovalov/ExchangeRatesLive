package com.vitovalov.exchangerateslive.internal.di

import com.vitovalov.exchangerateslive.presentation.view.ExchangeRatesListActivity
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityModule : AndroidInjector<ExchangeRatesListActivity> {

    @ContributesAndroidInjector
    fun contributeExchangeRatesListActivity(): ExchangeRatesListActivity
}
