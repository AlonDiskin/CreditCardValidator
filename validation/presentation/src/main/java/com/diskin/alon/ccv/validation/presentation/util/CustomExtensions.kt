package com.diskin.alon.ccv.validation.presentation.util

import android.text.InputType
import android.widget.EditText

fun EditText.setReadOnly(value: Boolean, inputType: Int = InputType.TYPE_NULL) {
    isFocusable = !value
    isFocusableInTouchMode = !value
    this.inputType = inputType
}