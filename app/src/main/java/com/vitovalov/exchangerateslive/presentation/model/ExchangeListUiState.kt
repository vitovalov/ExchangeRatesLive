package com.vitovalov.exchangerateslive.presentation.model

sealed class ExchangeListUiState {
    data class Loading(val message: String) : ExchangeListUiState()
    data class Error(val throwable: Throwable) : ExchangeListUiState()
    data class Update(val currencyExchangeList: List<ExchangeRatesUiModel>) : ExchangeListUiState()
}