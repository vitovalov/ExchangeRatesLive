package com.vitovalov.exchangerateslive.data.network

import com.vitovalov.exchangerateslive.data.network.model.ExchangeRatesResponse
import com.vitovalov.exchangerateslive.domain.model.ExchangeRatesModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Test
import java.io.IOException
import java.util.*

class ExchangeRatesRevoNetworkDatasourceTest {

    private val apiService = mockk<ExchangeRatesApiService>()
    private val networkDatasource =
        ExchangeRatesRevoNetworkDatasource(apiService)

    @Test
    fun `given base currency and correct API answer returns exchange rates model`() {
        //given
        val baseCurrency = "EUR"
        val ratesResponse = ExchangeRatesResponse(baseCurrency, emptyMap())
        every { apiService.getRatesForBase(any()) } returns Single.just(ratesResponse)

        //when
        val testObserver = networkDatasource.getExchangeRatesForBase(
            Currency.getInstance(baseCurrency)
        ).test()

        //then
        verify { apiService.getRatesForBase(Currency.getInstance(baseCurrency)) }
        testObserver
            .assertComplete()
            .assertValue(ExchangeRatesModel(Currency.getInstance(baseCurrency), emptyMap()))
            .assertNoErrors()
    }

    @Test
    fun `given base currency and incorrect API answer returns error`() {
        val baseCurrency = Currency.getInstance("EUR")
        every { apiService.getRatesForBase(any()) } returns Single.error(IOException())

        //when
        val testObserver =
            networkDatasource.getExchangeRatesForBase(baseCurrency).test()

        //then
        verify { apiService.getRatesForBase(baseCurrency) }
        testObserver
            .assertNoValues()
            .assertError(IOException::class.java)
    }
}