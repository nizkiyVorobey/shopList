package com.example.shoppinglist.domain

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

internal fun String?.removeTrimsOrEmpty() = this?.trim() ?: ""

internal fun <T> lazySimple(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)

internal inline fun TextView.setOnTextChange(crossinline block: () -> Unit) =
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
        override fun afterTextChanged(p0: Editable?) = Unit

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = block()
    })

internal fun TextView.setAfterTextChange(block: () -> Unit) =
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

        override fun afterTextChanged(p0: Editable?) = block()
    })
