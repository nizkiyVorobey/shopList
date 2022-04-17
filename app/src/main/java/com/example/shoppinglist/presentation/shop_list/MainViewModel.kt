package com.example.shoppinglist.presentation.shop_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.domain.DeleteShopItemUseCase
import com.example.shoppinglist.domain.EditShopItemUseCase
import com.example.shoppinglist.domain.GetShopListUseCase
import com.example.shoppinglist.domain.ShopItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getShopListUseCase: GetShopListUseCase,
    private val deleteShopItemUseCase: DeleteShopItemUseCase,
    private val editShopItemUseCase: EditShopItemUseCase
) : ViewModel() {

    /**
     * suspend функції можна викликати з іншої suspend функції  або з coroutine.
     * Кожна coroutine має запускатися в середені якосогось scope з якимось життєвим циклом
     */
    private val scope = CoroutineScope(Dispatchers.IO)

    val shopList = getShopListUseCase.getShopList()

    fun deleteShopItem(shopItem: ShopItem) {
        scope.launch {
            deleteShopItemUseCase.deleteShopItem(shopItem)
        }
    }

    fun changeEnableState(shopItem: ShopItem) {
        val editedShopItem = shopItem.copy(enabled = !shopItem.enabled)
        /**
         * CoroutineScope() і viewModelScope це одне і теж, але viewModelScope робить scope.cancel()
         * у метода onCleared за нас
         */
        viewModelScope.launch {
            editShopItemUseCase.editShopItem(editedShopItem)
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}