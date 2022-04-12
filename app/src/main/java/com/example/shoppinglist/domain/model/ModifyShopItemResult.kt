package com.example.shoppinglist.domain.model

sealed class ModifyShopItemResult {

    object Success : ModifyShopItemResult()

    data class ValidationError(
        val nameError: Boolean,
        val countError: Boolean
    ) : ModifyShopItemResult() {
        fun hasErrors(): Boolean = nameError || countError
    }
}
