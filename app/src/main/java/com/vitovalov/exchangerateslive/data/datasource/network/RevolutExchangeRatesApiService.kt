package com.vitovalov.exchangerateslive.data.datasource.network

import com.vitovalov.exchangerateslive.data.datasource.network.model.ExchangeRatesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface RevolutExchangeRatesApiService {

    @GET("latest")
    fun getRatesForBase(@Query("base") baseCurrency: Currency): Single<ExchangeRatesResponse>
}