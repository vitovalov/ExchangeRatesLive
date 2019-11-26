package com.vitovalov.exchangerateslive.internal.di

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.vitovalov.exchangerateslive.BuildConfig
import com.vitovalov.exchangerateslive.internal.Config
import com.vitovalov.exchangerateslive.data.datasource.network.ExchangeRatesNetworkService
import com.vitovalov.exchangerateslive.data.datasource.network.RevolutExchangeRatesApiService
import com.vitovalov.exchangerateslive.data.datasource.network.RevolutExchangeRatesNetworkService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkServiceModule {

    @Singleton
    @Provides
    fun providesExchangeRatesNetworkService(service: RevolutExchangeRatesApiService): ExchangeRatesNetworkService =
        RevolutExchangeRatesNetworkService(service)

    @Singleton
    @Provides
    fun providesRevolutExchangeRatesApiService(retrofit: Retrofit): RevolutExchangeRatesApiService =
        retrofit.create(RevolutExchangeRatesApiService::class.java)


    @Singleton
    @Provides
    fun providesRetrofit(okhttp3: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(okhttp3)
            .baseUrl(Config.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    @Singleton
    @Provides
    fun providesOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        return interceptor
    }
}