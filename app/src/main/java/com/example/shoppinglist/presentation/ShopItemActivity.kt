package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout
import java.lang.Exception
import java.lang.RuntimeException

class ShopItemActivity : AppCompatActivity() {
    private lateinit var viewModel: ShopItemViewModel
    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    private lateinit var shopItemName: TextInputLayout
    private lateinit var shopItemCount: TextInputLayout
    private lateinit var editItemName: EditText
    private lateinit var editItemCount: EditText
    private lateinit var shopItemSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)

        parseIntent()

        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        viewModel.shouldCloseScreen.observe(this) {
            finish()
        }

        initViews()
        launchRightMode()
        observeInputErrors()
        addTextChangeListeners()
    }


    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("param screen mode is absent")
        }

        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("unknown param screen mode $mode")
        }

        screenMode = mode

        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("param shop item id not found")
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun initViews() {
        shopItemName = findViewById(R.id.shop_item_name)
        shopItemCount = findViewById(R.id.shop_item_count)
        editItemName = findViewById(R.id.edit_shop_item_name)
        editItemCount = findViewById(R.id.edit_shop_item_count)
        shopItemSave = findViewById(R.id.shop_item_save)
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }
    }

    private fun launchAddMode() {
        shopItemSave.setOnClickListener {
            viewModel.addShopItem(editItemName.text?.toString(), editItemCount.text?.toString())
        }
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)

        viewModel.shopItem.observe(this) {
            editItemName.setText(it.name)
            editItemCount.setText(it.count.toString())
        }

        shopItemSave.setOnClickListener {
            viewModel.editShopItem(editItemName.text?.toString(), editItemCount.text?.toString())
        }
    }

    private fun observeInputErrors() {

        viewModel.errorInputName.observe(this) {
            if (it) {
                shopItemName.error = getString(R.string.error_shop_item_edit_name)
            } else {
                shopItemName.error = null
            }
        }

        viewModel.errorInputCount.observe(this) {
            if (it) {
                shopItemCount.error = getString(R.string.error_shop_item_edit_count)
            } else {
                shopItemCount.error = null
            }
        }

    }

    private fun addTextChangeListeners() {
        editItemName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        editItemCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.resetErrorInputCount()
            }

        })
    }


    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)

            return intent
        }

        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)

            return intent
        }
    }
}