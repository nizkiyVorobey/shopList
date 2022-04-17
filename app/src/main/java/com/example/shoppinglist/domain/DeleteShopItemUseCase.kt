package com.example.shoppinglist.domain

import javax.inject.Inject

class DeleteShopItemUseCase @Inject constructor(private val shopListRepository: ShopListRepository) {
    suspend fun deleteShopItem(item: ShopItem) {
        shopListRepository.deleteShopItem(item)
    }
}