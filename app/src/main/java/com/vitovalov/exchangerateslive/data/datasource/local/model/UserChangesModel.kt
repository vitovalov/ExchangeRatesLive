package com.vitovalov.exchangerateslive.data.datasource.local.model

import java.math.BigDecimal
import java.util.*

data class UserChangesModel(val baseCurrency: Currency,
                            var currencyAmount: BigDecimal)