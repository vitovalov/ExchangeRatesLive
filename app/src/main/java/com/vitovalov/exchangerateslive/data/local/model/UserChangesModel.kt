package com.vitovalov.exchangerateslive.data.local.model

import java.math.BigDecimal
import java.util.*

data class UserChangesModel(val baseCurrency: Currency,
                            var currencyAmount: BigDecimal)