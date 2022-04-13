package com.example.shoppinglist.di

import android.content.Context
import com.example.shoppinglist.data.AppDatabase
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.DeleteShopItemUseCase
import com.example.shoppinglist.domain.GetShopItemUseCase
import com.example.shoppinglist.domain.GetShopListUseCase
import com.example.shoppinglist.domain.ShopListRepository
import com.example.shoppinglist.domain.modify.AddShopItemUseCase
import com.example.shoppinglist.domain.modify.EditShopItemUseCase
import kotlin.properties.Delegates.notNull

object DependencyProvider {

    private var shopListRepository: ShopListRepositoryImpl? = null
    private var context: Context by notNull()
    private var isContextExist = false

    fun provideContext(context: Context) = apply {
        if (isContextExist) error("Context has already been set")

        this.context = context
        isContextExist = true
    }

    fun provideGetItemUseCase() = GetShopItemUseCase(provideRepository())
    fun provideEditItemUseCase() = EditShopItemUseCase(provideRepository())
    fun provideAddItemUseCase() = AddShopItemUseCase(provideRepository())
    fun provideDeleteItemUseCase() = DeleteShopItemUseCase(provideRepository())

    fun provideGetShopListUseCase() = GetShopListUseCase(provideRepository())

    private fun provideRepository(): ShopListRepository {
        return shopListRepository ?: ShopListRepositoryImpl(AppDatabase.getInstance(context))
            .also { shopListRepository = it }
    }
}