package com.example.shoppinglist.domain.modify

import com.example.shoppinglist.domain.model.ModifyShopItemResult

abstract class ModifyUseCase {

    protected fun parseCount(inputCount: String?): Int =
        runCatching { inputCount!!.trim().toInt() }.getOrDefault(0)

    // TODO: think about how to remove redundant ModifyShopItemResult and use errors
    protected fun validateInput(name: String, count: Int): ModifyShopItemResult.ValidationError {
        var error = ModifyShopItemResult.ValidationError(nameError = false, countError = false)
        if (count <= 0) {
            error = error.copy(countError = true)
        }
        if (name.isEmpty()) {
            error = error.copy(nameError = true)
        }
        return error
    }
}

sealed class ShopListError(message: String? = null) : Throwable(message) {

    sealed class General(message: String? = null) : ShopListError(message)

    sealed class Modify(message: String? = null) : ShopListError() {
        object InvalidName : Modify()
        object InvalidCount : Modify()
        object InvalidCredentials : Modify()
    }
}