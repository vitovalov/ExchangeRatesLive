package com.vitovalov.exchangerateslive.domain

import com.vitovalov.exchangerateslive.data.ExchangeRatesDatasource
import com.vitovalov.exchangerateslive.data.local.UserChangesLocalDatasource
import com.vitovalov.exchangerateslive.data.local.model.UserChangesModel
import com.vitovalov.exchangerateslive.data.network.ExchangeRatesNetworkDatasource
import com.vitovalov.exchangerateslive.domain.model.ExchangeRatesModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class ExchangeRatesDatasourceTest {

    private val localDatasource = mockk<UserChangesLocalDatasource>()
    private val networkDatasource = mockk<ExchangeRatesNetworkDatasource>()

    private val ratesRepository = ExchangeRatesDatasource(networkDatasource, localDatasource)

    @Test
    fun `when remote service responds OK then should return periodic exchange rates update`() {
        //given
        val baseCurrency = Currency.getInstance("EUR")
        val currencyExchangeRatesModel =
            ExchangeRatesModel(baseCurrency, emptyMap())
        every { networkDatasource.getExchangeRatesForBase(any()) } returns Single.just(
            currencyExchangeRatesModel
        )

        //when
        val testObserver =
            ratesRepository.observeExchangeRates(baseCurrency).test()

        //then
        verify { networkDatasource.getExchangeRatesForBase(any()) }
        testObserver
            .awaitCount(2)
            .assertNoErrors()
            .assertValueAt(0, currencyExchangeRatesModel)
    }

    @Test
    fun `given correct answers from local service should return correct user changes`() {
        //given
        val baseCurrency = Currency.getInstance("EUR")
        val amount = BigDecimal("100.0")
        every { localDatasource.observeUserChanges() } returns Flowable.just(
            UserChangesModel(baseCurrency, amount)
        )

        //when
        val testObserver = ratesRepository.observeUserCurrencySelection().test()

        //then
        verify { localDatasource.observeUserChanges() }
        testObserver
            .awaitCount(1)
            .assertNoErrors()
            .assertValueAt(0, UserChangesModel(baseCurrency, amount))
    }

    @Test
    fun `given new base when make a user base changes should complete`() {
        //given
        val baseCurrency = Currency.getInstance("EUR")
        every { localDatasource.updateUserBaseCurrency(baseCurrency) } returns Completable.complete()

        //when
        val testObserver =
            ratesRepository.updateUserBaseCurrency(baseCurrency).test()

        //then
        verify { localDatasource.updateUserBaseCurrency(baseCurrency) }
        testObserver
            .assertComplete()
            .assertNoErrors()
    }

    @Test
    fun `when user amount changes should update amount OK`() {
        //given
        val newAmount = BigDecimal("300.0")
        every { localDatasource.updateUserAmount(newAmount) } returns Completable.complete()

        //when
        val testObserver =
            ratesRepository.updateUserAmount(newAmount).test()

        //then
        verify { localDatasource.updateUserAmount(newAmount) }
        testObserver
            .assertComplete()
            .assertNoErrors()
    }
}