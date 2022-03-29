package com.example.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository
import java.lang.RuntimeException
import kotlin.random.Random

/**
 * singleton
 * Це потрібно, щоб не вийшло так, що на одному екрані ми працюємо з одним репозиторієм, а на іншому з іншим
 */
object ShopListRepositoryImpl : ShopListRepository {
    private val shopListLD = MutableLiveData<List<ShopItem>>()

    // Сорутємо при кожній зміні shopList по id, щоб змінений item не потрапляв до низу списку
    private val shopList = sortedSetOf<ShopItem>({ o1, o2 -> o1.id.compareTo(o2.id) })


    private var autoIncrementId = 0

    init {
        for (i in 0..1_0) {
            val item = ShopItem("Name $i", i, Random.nextBoolean())
            addShopItem(item)
        }
    }

    override fun addShopItem(item: ShopItem) {
        if (item.id == ShopItem.UNDEFINED_ID) {
            item.id = autoIncrementId
            autoIncrementId++
        }
        shopList.add(item)
        updateList()
    }

    override fun editShopItem(item: ShopItem) {
        val oldElement = getShopItem(item.id)
        shopList.remove(oldElement)
        addShopItem(item)
    }

    override fun deleteShopItem(item: ShopItem) {
        shopList.remove(item)
        updateList()
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListLD
    }

    override fun getShopItem(id: Int): ShopItem {
        return shopList.find { it.id == id }
            ?: throw RuntimeException("Element with id $id not found")
    }

    private fun updateList() {
        shopListLD.postValue(shopList.toList()) // .toList create copy of shopList. This more safety, we can't mutate the collection out of this class
    }
}