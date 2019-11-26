package com.vitovalov.exchangerateslive.presentation

import androidx.lifecycle.ViewModel
import com.vitovalov.exchangerateslive.domain.ObserveExchangeRatesUseCase
import com.vitovalov.exchangerateslive.presentation.model.ExchangeListUiState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import javax.inject.Inject

class ExchangeRatesListViewModel @Inject constructor(private val observeExchangeRatesUseCase: ObserveExchangeRatesUseCase) :
        ViewModel() {

    private var exchangeRatesDisposable: Disposable? = null

    fun observeExchangeListState(flowableSubscriber: DisposableSubscriber<ExchangeListUiState>) {
        flowableSubscriber.onNext(ExchangeListUiState.Loading("Loading..."))
        exchangeRatesDisposable?.dispose()
        observeExchangeRatesUseCase.run()
                .map { ExchangeListUiState.Update(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(flowableSubscriber)
                .let { exchangeRatesDisposable = it }
    }

    override fun onCleared() {
        super.onCleared()
        exchangeRatesDisposable?.dispose()
    }
}