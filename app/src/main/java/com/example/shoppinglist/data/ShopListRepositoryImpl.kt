package com.example.shoppinglist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository

/**
 * singleton
 * Це потрібно, щоб не вийшло так, що на одному екрані ми працюємо з одним репозиторієм, а на іншому з іншим
 */
class ShopListRepositoryImpl(
    application: Application
) : ShopListRepository {
    private val shopListDao = AppDatabase.getInstance(application).shopListDao()
    private val mapper = ShopListMapper()


    override fun addShopItem(item: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbMode(item))
    }


    override fun deleteShopItem(item: ShopItem) {
        shopListDao.deleteShopItem(item.id)
    }

    override fun editShopItem(item: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbMode(item))
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        val dbModel = shopListDao.getShopItem(shopItemId)
        return mapper.mapDbModeToEntity(dbModel)
    }

    /**
     * MediatorLiveData - перехоплює дані при зміні LiveData , потрібен коли ми маємо змінити тип LiveData,
     * або можимо оновити нашу LiveData лише при якійсь події
     */
//    override fun getShopList(): LiveData<List<ShopItem>> = MediatorLiveData<List<ShopItem>>().apply {
//        addSource(shopListDao.getShopList()) {
//            value = mapper.mapListDbModelToListEntity(it)
//        }
//    }

    /**
     * Transformations теж саме що MediatorLiveData, але лише для зміни типів, під капотом використовує той же MediatorLiveData
     */
    override fun getShopList(): LiveData<List<ShopItem>> = Transformations.map(shopListDao.getShopList()) {
        mapper.mapListDbModelToListEntity(it)
    }
}