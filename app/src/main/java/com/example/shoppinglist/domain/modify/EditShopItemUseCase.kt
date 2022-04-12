package com.example.shoppinglist.domain.modify

import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository
import com.example.shoppinglist.domain.model.ModifyShopItemResult
import com.example.shoppinglist.domain.removeTrimsOrEmpty

class EditShopItemUseCase(private val shopListRepository: ShopListRepository) : ModifyUseCase() {

    suspend fun editShopItem(
        oldShopItem: ShopItem,
        newName: String?,
        newCount: String?
    ): ModifyShopItemResult {

        val name = newName.removeTrimsOrEmpty()
        val count = super.parseCount(newCount)
        val error = super.validateInput(name, count)

        return when {
            error.hasErrors() -> error
            else -> {
                shopListRepository.editShopItem(oldShopItem.copy(name = name, count = count))
                ModifyShopItemResult.Success
            }
        }
    }
}