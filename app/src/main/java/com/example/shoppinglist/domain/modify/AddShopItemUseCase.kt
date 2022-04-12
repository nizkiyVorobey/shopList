package com.example.shoppinglist.domain.modify

import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository
import com.example.shoppinglist.domain.model.ModifyShopItemResult
import com.example.shoppinglist.domain.removeTrimsOrEmpty

class AddShopItemUseCase(private val shopListRepository: ShopListRepository) : ModifyUseCase() {

    suspend fun addShopItem(newName: String?, newCount: String?): ModifyShopItemResult {
        val name = newName.removeTrimsOrEmpty()
        val count = super.parseCount(newCount)
        val error = super.validateInput(name, count)
        return when {
            error.hasErrors() -> error
            else -> {
                val newShopItem = ShopItem(name = name, count = count, enabled = true)
                shopListRepository.addShopItem(newShopItem)
                ModifyShopItemResult.Success
            }
        }
    }
}