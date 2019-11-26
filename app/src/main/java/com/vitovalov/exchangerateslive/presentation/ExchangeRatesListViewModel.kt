package com.vitovalov.exchangerateslive.presentation

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import com.vitovalov.exchangerateslive.domain.UpdateBaseCurrencyUseCase
import com.vitovalov.exchangerateslive.domain.ObserveExchangeRatesUseCase
import com.vitovalov.exchangerateslive.domain.UpdateUserAmountUseCase
import com.vitovalov.exchangerateslive.presentation.model.ExchangeListUiState
import com.vitovalov.exchangerateslive.presentation.model.ExchangeRateUiModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import java.math.BigDecimal
import javax.inject.Inject

class ExchangeRatesListViewModel @Inject constructor(
    private val observeExchangeRatesUseCase: ObserveExchangeRatesUseCase,
    private val updateUserAmountUseCase: UpdateUserAmountUseCase,
    private val updateBaseCurrencyUseCase: UpdateBaseCurrencyUseCase
) :
    ViewModel() {

    private var exchangeRatesDisposable: Disposable? = null
    private var userAmountDisposable: Disposable? = null
    private var userBaseChangedDisposable: Disposable? = null

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

    fun onCurrencySelected(uiModel: ExchangeRateUiModel) {
        userBaseChangedDisposable?.dispose()
        updateBaseCurrencyUseCase.execute(uiModel.currency)
            .andThen(updateUserAmountUseCase.execute(uiModel.amount))
            .subscribe()
            .let { userBaseChangedDisposable = it }
    }

    fun onAmountChanged(newAmount: BigDecimal) {
        userAmountDisposable?.dispose()
        updateUserAmountUseCase.execute(newAmount)
            .subscribe()
            .let { userAmountDisposable = it }
    }

    fun onCalculateListsDifferences(
        oldList: List<ExchangeRateUiModel>,
        newList: List<ExchangeRateUiModel>
    ): Single<DiffUtil.DiffResult> {
        return Single.just(DiffUtil.calculateDiff(ExchangeRateItemDiffList(oldList, newList)))
            .subscribeOn(Schedulers.io())
    }

    override fun onCleared() {
        super.onCleared()
        exchangeRatesDisposable?.dispose()
        userAmountDisposable?.dispose()
        userBaseChangedDisposable?.dispose()
    }
}