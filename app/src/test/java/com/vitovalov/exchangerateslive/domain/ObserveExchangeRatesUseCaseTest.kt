package com.vitovalov.exchangerateslive.domain

import com.vitovalov.exchangerateslive.data.local.model.UserChangesModel
import com.vitovalov.exchangerateslive.domain.model.ExchangeRatesModel
import com.vitovalov.exchangerateslive.internal.Config
import com.vitovalov.exchangerateslive.presentation.model.ExchangeRatesUiModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Flowable
import org.junit.Test
import java.math.BigDecimal
import java.math.MathContext
import java.util.*

class ObserveExchangeRatesUseCaseTest {

    private val repository = mockk<ExchangeRatesRepository>()
    private val mathContext = MathContext(Config.MATH_PRECISION, Config.MATH_ROUNDING_MODE)
    private val useCase = ObserveExchangeRatesUseCase(repository, mathContext)

    @Test
    fun `when repository answers OK should return correct exchange rates`() {
        //given
        val baseCurrency = Currency.getInstance("EUR")
        val baseCurrencyAmount = BigDecimal("100.0")
        every { repository.observeExchangeRates(baseCurrency) } returns
                Flowable.just(
                    ExchangeRatesModel(
                        baseCurrency,
                        mapOf(Pair(Currency.getInstance("SEK"), BigDecimal("2.0")))
                    )
                )
        every { repository.observeUserCurrencySelection() } returns Flowable.just(
            UserChangesModel(
                baseCurrency,
                baseCurrencyAmount
            )
        )

        //when
        val testSubscriber = useCase.execute().test()

        //then
        verify { repository.observeExchangeRates(baseCurrency) }
        verify { repository.observeUserCurrencySelection() }
        testSubscriber
            .awaitCount(1)
            .assertNoErrors()
            .assertValue(
                listOf(
                    ExchangeRatesUiModel(Currency.getInstance("EUR"), BigDecimal("100.00")),
                    ExchangeRatesUiModel(Currency.getInstance("SEK"), BigDecimal("200.00"))
                )
            )
    }

    @Test
    fun `when user changes base currency should return matching exchange rates`() {
        //given
        val baseCurrency = Currency.getInstance("EUR")
        val userSelectionBaseCurrency = Currency.getInstance("USD")
        val userBaseCurrencyAmount = BigDecimal("100.0")
        every { repository.observeExchangeRates(baseCurrency) } returns
                Flowable.just(
                    ExchangeRatesModel(
                        baseCurrency, mapOf(
                            Pair(Currency.getInstance("SEK"), BigDecimal("3.0")), Pair(
                                Currency.getInstance("USD"), BigDecimal("2.0")
                            )
                        )
                    )
                )
        every { repository.observeUserCurrencySelection() } returns Flowable.just(
            UserChangesModel(
                userSelectionBaseCurrency,
                userBaseCurrencyAmount
            )
        )

        //when
        val testSubscriber = useCase.execute().test()

        //then
        verify { repository.observeExchangeRates(baseCurrency) }
        verify { repository.observeUserCurrencySelection() }
        testSubscriber
            .awaitCount(1)
            .assertNoErrors()
            .assertValue(
                listOf(
                    ExchangeRatesUiModel(Currency.getInstance("USD"), BigDecimal("100.0")),
                    ExchangeRatesUiModel(Currency.getInstance("EUR"), BigDecimal("50.0")),
                    ExchangeRatesUiModel(Currency.getInstance("SEK"), BigDecimal("150.0"))
                )
            )
    }
}
