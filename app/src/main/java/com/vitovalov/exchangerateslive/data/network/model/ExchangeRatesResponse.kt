package com.vitovalov.exchangerateslive.data.network.model

import com.google.gson.annotations.SerializedName

data class ExchangeRatesResponse(
    @SerializedName("base") val baseCurrency: String,
    @SerializedName("rates") val ratesMap: Map<String, String>
)