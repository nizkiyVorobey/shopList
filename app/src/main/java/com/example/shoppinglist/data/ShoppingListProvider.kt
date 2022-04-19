package com.example.shoppinglist.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.shoppinglist.ShopListApplication
import com.example.shoppinglist.domain.ShopItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShoppingListProvider : ContentProvider() {
    private val component by lazy {
        (context as ShopListApplication).component
    }

    @Inject
     lateinit var shopListDao: ShopListDao

     @Inject
     lateinit var mapper: ShopListMapper

    private val scope = CoroutineScope(Dispatchers.IO)

//    Path:
//    shop_items/# Якщо хочемо метчити якісь цифри пілся /. Наприклад shop_items/3
//    Або shop_items/* для строк, наприклад shop_items/3 чи shop_items/Jhon

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(
            "com.example.shoppinglist",
            "shop_items",
            GET_SHOP_ITEMS_QUERY
        )
        addURI(
            "com.example.shoppinglist",
            "shop_items/#",
            GET_SHOP_ITEM_BY_ID
        )
    }

    override fun onCreate(): Boolean {
        component.inject(this)
        return true // true Якщо створення ContentProvider відбулося успішно і false, якщо не цспішнр
    }

    override fun query(
        uri: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            GET_SHOP_ITEMS_QUERY -> {
                shopListDao.getShopListCursor() // Повертаємо Cursor, який бібліоткета Room стоврила
            }
            else -> null
        }
    }

    override fun getType(p0: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
         when (uriMatcher.match(uri)) {
            GET_SHOP_ITEMS_QUERY -> {
                if (values == null) return null

                val id = values.getAsInteger("id")
                val name = values.getAsString("name")
                val count = values.getAsInteger("count")
                val enabled = values.getAsBoolean("enabled")

                val shopItem = ShopItem(
                    id = id,
                    name = name,
                    count = count,
                    enabled = enabled,
                )

                scope.launch {
                    shopListDao.addShopItem(mapper.mapEntityToDbMode(shopItem))

                    scope.cancel()
                }
            }
        }

        return null
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("Not yet implemented")
    }



    companion object {
        private const val GET_SHOP_ITEMS_QUERY = 100
        private const val GET_SHOP_ITEM_BY_ID = 101
    }
}