package com.vitovalov.exchangerateslive.domain.model

import java.math.BigDecimal
import java.util.*

data class ExchangeRatesModel(val baseCurrency: Currency, val ratesMap: Map<Currency, BigDecimal>)