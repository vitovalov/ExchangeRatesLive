package com.vitovalov.exchangerateslive.internal.di

import android.app.Application
import com.vitovalov.exchangerateslive.data.local.RoomUserChangesDb
import com.vitovalov.exchangerateslive.data.local.UserChangesLocalDatasource
import com.vitovalov.exchangerateslive.data.local.UserChangesRoomDatasource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalDatasourceModule {

    @Singleton
    @Provides
    fun providesUserChangesLocalDatasource(db: RoomUserChangesDb): UserChangesLocalDatasource =
        UserChangesRoomDatasource(db)

    @Singleton
    @Provides
    fun providesRoomCurrencyExchangeRatesDatabase(application: Application): RoomUserChangesDb =
        RoomUserChangesDb.getInstance(application)
}