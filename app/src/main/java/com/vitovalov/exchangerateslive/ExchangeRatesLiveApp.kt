package com.vitovalov.exchangerateslive

import android.app.Activity
import android.app.Application
import com.vitovalov.exchangerateslive.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class ExchangeRatesLiveApp : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerApplicationComponent.builder()
            .withApplicationContext(this)
            .build()
            .inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector
}