package com.vitovalov.exchangerateslive

import android.app.Application
import com.vitovalov.exchangerateslive.internal.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class ExchangeRatesLiveApp : Application(), HasAndroidInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        DaggerApplicationComponent.builder()
            .withApplicationContext(this)
            .build()
            .inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = activityInjector
}