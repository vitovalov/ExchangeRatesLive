package com.vitovalov.exchangerateslive.data

import com.vitovalov.exchangerateslive.data.local.UserChangesLocalDatasource
import com.vitovalov.exchangerateslive.data.local.model.UserChangesModel
import com.vitovalov.exchangerateslive.data.network.ExchangeRatesNetworkDatasource
import com.vitovalov.exchangerateslive.domain.ExchangeRatesRepository
import com.vitovalov.exchangerateslive.domain.model.ExchangeRatesModel
import com.vitovalov.exchangerateslive.internal.Config
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.TimeUnit

class ExchangeRatesDatasource(
    private val networkDatasource: ExchangeRatesNetworkDatasource,
    private val localDatasource: UserChangesLocalDatasource
) : ExchangeRatesRepository {

    override fun observeExchangeRates(baseCurrency: Currency): Flowable<ExchangeRatesModel> =
        networkDatasource.getExchangeRatesForBase(baseCurrency)
            .repeatWhen {
                it.delay(Config.TIME_INTERVAL_BETWEEN_NETWORK_REFRESHES, TimeUnit.SECONDS)
            }.retry(1)
            .subscribeOn(Schedulers.computation())

    override fun observeUserCurrencySelection(): Flowable<UserChangesModel> =
        localDatasource.observeUserChanges()
            .subscribeOn(Schedulers.computation())


    override fun updateUserAmount(newAmount: BigDecimal) =
        localDatasource.updateUserAmount(newAmount)

    override fun updateUserBaseCurrency(baseCurrency: Currency) =
        localDatasource.updateUserBaseCurrency(baseCurrency)
}