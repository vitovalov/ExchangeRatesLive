package com.vitovalov.exchangerateslive.data.local

import com.vitovalov.exchangerateslive.data.local.model.UserChangesModel
import io.reactivex.Completable
import io.reactivex.Flowable
import java.math.BigDecimal
import java.util.*

interface UserChangesLocalDatasource {

    fun observeUserChanges(): Flowable<UserChangesModel>
    fun updateUserAmount(newAmount: BigDecimal): Completable
    fun updateUserBaseCurrency(newBaseCurrency: Currency): Completable
}
