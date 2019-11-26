package com.vitovalov.exchangerateslive.data.datasource

import com.vitovalov.exchangerateslive.data.datasource.network.ExchangeRatesNetworkService
import com.vitovalov.exchangerateslive.domain.ExchangeRatesRepository
import com.vitovalov.exchangerateslive.domain.model.ExchangeRatesModel
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class ExchangeRatesDatasource(private val remoteService: ExchangeRatesNetworkService) :
        ExchangeRatesRepository {

    override fun observeExchangeRates(baseCurrency: Currency): Flowable<ExchangeRatesModel> =
            remoteService.getExchangeRatesForBase(baseCurrency)
                    .repeatWhen {
                        it.delay(1, TimeUnit.SECONDS)
                    }
                    .retry(1)
                    .subscribeOn(Schedulers.computation())


}