package com.example.shoppinglist.domain

class DeleteShopItemUseCase(private val shopListRepository: ShopListRepository) {
    fun deleteShopItem(id: Int) {
        shopListRepository.deleteShopItem(id)
    }
}