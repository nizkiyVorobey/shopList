package com.example.shoppinglist.domain

internal fun String?.removeTrimsOrEmpty() = this?.trim() ?: ""
