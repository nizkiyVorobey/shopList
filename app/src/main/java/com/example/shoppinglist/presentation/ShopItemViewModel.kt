package com.example.shoppinglist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.lang.Exception

class ShopItemViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ShopListRepositoryImpl(application)

    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)

    private val scope = CoroutineScope(Dispatchers.IO)

    /**
     * Оскілкьки ми хочемо в середені ViewModel працювати з MutableLiveData, а з Activity заборонити встановлювати значення
     * то нам підійде варіант створити _errorInputName для ViewModel з типом MutableLiveData, а от для Activity в нас
     * буде errorInputName у якої ми перевизначемо getter
     */
    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen


    fun getShopItem(shopItemId: Int) {
        scope.launch {
            val item = getShopItemUseCase.getShopItem(shopItemId)
            _shopItem.postValue(item)
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val isFieldsValid = validateInput(name, count)

        if (isFieldsValid) {
            val shopItem = _shopItem.value
            shopItem?.let {
                val newShopItem = it.copy(name = name, count = count)
                scope.launch {
                    editShopItemUseCase.editShopItem(newShopItem)
                }
            }

            finishWork()
        }

    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val isFieldsValid = validateInput(name, count)

        if (isFieldsValid) {
            val newShopItem = ShopItem(name = name, count = count, enabled = true)
            scope.launch {
                addShopItemUseCase.addShopItem(newShopItem)
            }
            finishWork()
        }
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        if (count <= 0) {
            result = false
            _errorInputCount.postValue(true)
        }

        if (name.isEmpty()) {
            result = false
            _errorInputName.postValue(true)
        }

        return result
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