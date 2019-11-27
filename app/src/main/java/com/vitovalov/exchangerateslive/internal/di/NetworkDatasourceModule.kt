package com.vitovalov.exchangerateslive.internal.di

import android.util.Base64
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.vitovalov.exchangerateslive.BuildConfig
import com.vitovalov.exchangerateslive.data.network.ExchangeRatesApiService
import com.vitovalov.exchangerateslive.data.network.ExchangeRatesNetworkDatasource
import com.vitovalov.exchangerateslive.data.network.ExchangeRatesRevoNetworkDatasource
import com.vitovalov.exchangerateslive.internal.Config
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import javax.inject.Singleton

@Module
class NetworkDatasourceModule {

    @Singleton
    @Provides
    fun providesExchangeRatesNetworkService(service: ExchangeRatesApiService): ExchangeRatesNetworkDatasource =
        ExchangeRatesRevoNetworkDatasource(service)

    @Singleton
    @Provides
    fun providesRevoExchangeRatesApiService(retrofit: Retrofit): ExchangeRatesApiService =
        retrofit.create(ExchangeRatesApiService::class.java)


    @Singleton
    @Provides
    fun providesRetrofit(okhttp3: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(okhttp3)
        .baseUrl(decodeBaseUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    private fun decodeBaseUrl(): String {
        val bytes = Base64.decode(Config.API_BASE_URL, Base64.NO_WRAP)
        var baseUrl = "www.google.com/"
        try {
            baseUrl = String(bytes, Charset.forName("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return baseUrl
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            // .addInterceptor(loggingInterceptor) // uncomment to see request logs
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