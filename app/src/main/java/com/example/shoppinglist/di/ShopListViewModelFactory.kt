package com.example.shoppinglist.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.presentation.main.ShopListViewModel
import com.example.shoppinglist.presentation.shop_item.ShopItemViewModel

class ShopListViewModelFactory : ViewModelProvider.Factory {

    private val di: DependencyProvider get() = DependencyProvider

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(ShopItemViewModel::class.java) ->
            ShopItemViewModel(
                getUseCase = di.provideGetItemUseCase(),
                editUseCase = di.provideEditItemUseCase(),
                addUseCase = di.provideAddItemUseCase()
            )
        modelClass.isAssignableFrom(ShopListViewModel::class.java) ->
            ShopListViewModel(
                getUseCase = di.provideGetShopListUseCase(),
                deleteUseCase = di.provideDeleteItemUseCase(),
            )
        else -> throw IllegalArgumentException("${modelClass.canonicalName} is not supported")
    } as T
}