package com.vitovalov.exchangerateslive.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vitovalov.exchangerateslive.data.datasource.local.model.UserChangesEntity
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface UserChangesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userChangesEntity: UserChangesEntity): Completable

    @Query("SELECT * FROM user_currency_changes")
    fun observeUserAmountChanges(): Flowable<UserChangesEntity>

    @Query("UPDATE user_currency_changes SET selected_currency_amount = :currencyAmount")
    fun updateUserAmount(currencyAmount: String): Completable

    @Query("UPDATE user_currency_changes SET selected_currency_code = :baseCurrencyCode")
    fun updateUserBaseCurrency(baseCurrencyCode: String): Completable
}