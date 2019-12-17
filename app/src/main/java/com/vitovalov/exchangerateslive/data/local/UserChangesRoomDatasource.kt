package com.vitovalov.exchangerateslive.data.local

import com.vitovalov.exchangerateslive.data.local.model.UserChangesModel
import io.reactivex.Completable
import io.reactivex.Flowable
import java.math.BigDecimal
import java.util.*

class UserChangesRoomDatasource(private val roomDb: RoomUserChangesDb) : UserChangesLocalDatasource {

    override fun observeUserChanges(): Flowable<UserChangesModel> =
            roomDb
                    .userChangesDao()
                    .observeUserAmountChanges()
                    .map {
                        println("inside map of roomDB")
                        UserChangesModel(
                                Currency.getInstance(it.selectedCurrencyCode),
                                BigDecimal(it.selectedCurrencyAmount)
                        )
                    }

    override fun updateUserBaseCurrency(newBaseCurrency: Currency): Completable =
            roomDb
                    .userChangesDao()
                    .updateUserBaseCurrency(newBaseCurrency.currencyCode)

    override fun updateUserAmount(newCurrencyAmount: BigDecimal): Completable =
            roomDb
                    .userChangesDao()
                    .updateUserAmount(newCurrencyAmount.toPlainString())
}