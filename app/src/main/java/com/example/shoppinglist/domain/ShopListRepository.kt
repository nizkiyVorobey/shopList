package com.example.shoppinglist.domain

interface ShopListRepository {
    fun addShopItem(item: ShopItem)

    fun editShopItem(item: ShopItem)

    fun deleteShopItem(item: ShopItem)

    fun getShopList(): List<ShopItem>

    fun getShopItem(id: Int): ShopItem
}