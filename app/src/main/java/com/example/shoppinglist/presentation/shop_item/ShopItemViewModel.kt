package com.example.shoppinglist.presentation.shop_item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.domain.GetShopItemUseCase
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.model.ModifyShopItemResult.Success
import com.example.shoppinglist.domain.model.ModifyShopItemResult.ValidationError
import com.example.shoppinglist.domain.modify.AddShopItemUseCase
import com.example.shoppinglist.domain.modify.EditShopItemUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ShopItemViewModel(
    private val getUseCase: GetShopItemUseCase,
    private val editUseCase: EditShopItemUseCase,
    private val addUseCase: AddShopItemUseCase,
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)

    /**
     * Оскілкьки ми хочемо в середені ViewModel працювати з MutableLiveData, а з Activity заборонити встановлювати значення
     * то нам підійде варіант створити _errorInputName для ViewModel з типом MutableLiveData, а от для Activity в нас
     * буде errorInputName у якої ми перевизначемо getter
     */

    /*
        ShopItemState{
           nameError: Boolean,
           countError: Boolean,
           item: ShopItem
           shouldCloseScreen: Unit
        }
        LiveData<ShopItemState>
     */
    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean> = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean> = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem> = _shopItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit> = _shouldCloseScreen

    // Ui -> Action -> ViewModel[State] -> state(modify) ->

    fun getShopItem(shopItemId: Int) {
        scope.launch {
            val item = getUseCase.getShopItem(shopItemId)
            _shopItem.postValue(item)
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        scope.launch {
            val result = editUseCase.editShopItem(
                oldShopItem = _shopItem.value!!,
                newName = inputName,
                newCount = inputCount
            )
            when (result) {
                Success -> finishWork()
                is ValidationError -> proceedError(result)
            }
        }
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        scope.launch {
            val result = addUseCase.addShopItem(
                newName = inputName,
                newCount = inputCount
            )
            when (result) {
                Success -> finishWork()
                is ValidationError -> proceedError(result)
            }
        }
    }

    private fun proceedError(result: ValidationError) {
        when {
            result.nameError -> _errorInputName.postValue(true)
            result.countError -> _errorInputCount.postValue(true)
        }
    }

    fun resetErrorInputName() {
        _errorInputName.postValue(false)
    }

    fun resetErrorInputCount() {
        _errorInputCount.postValue(false)
    }

    private fun finishWork() {
        _shouldCloseScreen.postValue(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}