package com.vitovalov.exchangerateslive.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_currency_changes")
data class UserChangesEntity(
    @PrimaryKey
    val selectionId: Int,
    @ColumnInfo(name = "selected_currency_code")
    val selectedCurrencyCode: String,
    @ColumnInfo(name = "selected_currency_amount")
    val selectedCurrencyAmount: String)