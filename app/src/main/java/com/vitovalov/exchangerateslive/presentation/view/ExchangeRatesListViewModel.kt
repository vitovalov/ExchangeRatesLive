package com.vitovalov.exchangerateslive.presentation.view

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import com.squareup.picasso.Picasso
import com.vitovalov.exchangerateslive.R
import com.vitovalov.exchangerateslive.domain.ObserveExchangeRatesUseCase
import com.vitovalov.exchangerateslive.domain.UpdateBaseCurrencyUseCase
import com.vitovalov.exchangerateslive.domain.UpdateUserAmountUseCase
import com.vitovalov.exchangerateslive.presentation.model.ExchangeListUiState
import com.vitovalov.exchangerateslive.presentation.model.ExchangeRateUiModel
import com.vitovalov.exchangerateslive.presentation.view.uiutils.ExchangeRateItemDiffList
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import jp.wasabeef.picasso.transformations.CropTransformation
import java.math.BigDecimal
import java.util.*
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

    fun calculateListsDifferences(
        oldList: List<ExchangeRateUiModel>,
        newList: List<ExchangeRateUiModel>
    ): Single<DiffUtil.DiffResult> {
        return Single.just(
            DiffUtil.calculateDiff(
                ExchangeRateItemDiffList(
                    oldList,
                    newList
                )
            )
        )
            .subscribeOn(Schedulers.io())
    }

    fun loadItemImage(imageView: ImageView, item: ExchangeRateUiModel) {
        Picasso.get()
            .load("https://www.countryflags.io/${getCountryCode(item.currency)}/shiny/64.png")
            .placeholder(R.drawable.image_placeholder)
            .transform(
                CropTransformation(
                    0.5f,
                    0.5f,
                    CropTransformation.GravityHorizontal.CENTER,
                    CropTransformation.GravityVertical.CENTER
                )
            )
            .transform(CropCircleTransformation())
            .into(imageView)
    }

    private fun getCountryCode(currencyModel: Currency) =
        if (currencyModel.currencyCode == "EUR")
            "EU"
        else Locale.getAvailableLocales().find {
            try {
                Currency.getInstance(it) == currencyModel
            } catch (e: Exception) {
                return@find false
            }
        }?.country ?: ""


    override fun onCleared() {
        super.onCleared()
        exchangeRatesDisposable?.dispose()
        userAmountDisposable?.dispose()
        userBaseChangedDisposable?.dispose()
    }
}