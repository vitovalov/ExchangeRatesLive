package com.vitovalov.exchangerateslive.presentation.view.uiutils

import android.text.Editable
import android.text.TextWatcher
import java.math.BigDecimal

class AmountInputTextWatcher(private val amountChanged: (BigDecimal) -> Unit) : TextWatcher {

    override fun afterTextChanged(p0: Editable?) {}

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(amountValue: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (!amountValue.isNullOrBlank()) {
            try {
                amountChanged(BigDecimal(amountValue.toString()))
            } catch (e: NumberFormatException) {
                amountChanged(BigDecimal("0"))
            }
        }
    }
}