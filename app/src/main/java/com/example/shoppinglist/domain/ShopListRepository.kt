package com.example.shoppinglist.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {
    fun addShopItem(item: ShopItem)

    fun editShopItem(item: ShopItem)

    fun deleteShopItem(item: ShopItem)

    fun getShopList(): LiveData<List<ShopItem>>

    fun getShopItem(id: Int): ShopItem
}