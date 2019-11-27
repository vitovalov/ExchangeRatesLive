package com.vitovalov.exchangerateslive.internal

import java.math.RoundingMode
import java.util.*

object Config {
    const val API_BASE_URL = "***REMOVED***"
    const val TIME_INTERVAL_BETWEEN_NETWORK_REFRESHES: Long = 1
    const val MATH_PRECISION: Int = 6
    val START_BASE_CURRENCY: Currency = Currency.getInstance("EUR")
    val MATH_ROUNDING_MODE = RoundingMode.HALF_EVEN
}