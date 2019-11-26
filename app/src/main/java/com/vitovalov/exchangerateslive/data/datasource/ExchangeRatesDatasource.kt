package com.vitovalov.exchangerateslive.data.datasource

import com.vitovalov.exchangerateslive.data.datasource.local.UserChangesLocalDatasource
import com.vitovalov.exchangerateslive.data.datasource.local.model.UserChangesModel
import com.vitovalov.exchangerateslive.data.datasource.network.ExchangeRatesNetworkDatasource
import com.vitovalov.exchangerateslive.domain.ExchangeRatesRepository
import com.vitovalov.exchangerateslive.domain.model.ExchangeRatesModel
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.TimeUnit

class ExchangeRatesDatasource(
    private val networkDatasource: ExchangeRatesNetworkDatasource,
    private val localDatasource: UserChangesLocalDatasource
) :
    ExchangeRatesRepository {

    override fun observeExchangeRates(baseCurrency: Currency): Flowable<ExchangeRatesModel> =
        networkDatasource.getExchangeRatesForBase(baseCurrency)
            .repeatWhen {
                it.delay(1, TimeUnit.SECONDS)
            }
            .retry(1)
            .subscribeOn(Schedulers.computation())

    override fun observeUserCurrencySelection(): Flowable<UserChangesModel> =
        localDatasource.observeUserChanges()
            .subscribeOn(Schedulers.computation())


    override fun updateUserAmount(newAmount: BigDecimal) =
        localDatasource.updateUserAmount(newAmount)

    override fun updateUserBaseCurrency(baseCurrency: Currency) =
        localDatasource.updateUserBaseCurrency(baseCurrency)
}