package com.vitovalov.exchangerateslive.presentation

import androidx.lifecycle.ViewModel
import com.vitovalov.exchangerateslive.domain.ObserveExchangeRatesUseCase
import com.vitovalov.exchangerateslive.domain.UpdateUserAmountUseCase
import com.vitovalov.exchangerateslive.presentation.model.ExchangeListUiState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import java.math.BigDecimal
import javax.inject.Inject

class ExchangeRatesListViewModel @Inject constructor(
        private val observeExchangeRatesUseCase: ObserveExchangeRatesUseCase,
        private val updateUserAmountUseCase: UpdateUserAmountUseCase
) :
        ViewModel() {

    private var exchangeRatesDisposable: Disposable? = null
    private var userAmountDisposable: Disposable? = null

    fun observeExchangeListState(flowableSubscriber: DisposableSubscriber<ExchangeListUiState>) {
        flowableSubscriber.onNext(ExchangeListUiState.Loading("Loading..."))
        exchangeRatesDisposable?.dispose()
        observeExchangeRatesUseCase.execute()
                .map { ExchangeListUiState.Update(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(flowableSubscriber)
                .let { exchangeRatesDisposable = it }
    }

    fun onAmountChanged(newAmount: BigDecimal) {
        userAmountDisposable?.dispose()
        updateUserAmountUseCase.execute(newAmount)
                .subscribe()
                .let { userAmountDisposable = it }
    }

    override fun onCleared() {
        super.onCleared()
        exchangeRatesDisposable?.dispose()
        userAmountDisposable?.dispose()
    }
}