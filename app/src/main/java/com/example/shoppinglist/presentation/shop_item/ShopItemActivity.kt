package com.example.shoppinglist.presentation.shop_item

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.presentation.shop_item.delegate.ScreenModeDelegate
import com.example.shoppinglist.presentation.shop_item.delegate.ScreenModeDelegate.Companion.MODE_ADD
import com.example.shoppinglist.presentation.shop_item.delegate.ScreenModeDelegate.Companion.MODE_EDIT

class ShopItemActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {
    private val intentParser = ScreenModeDelegate()
    private val screenMode by intentParser

    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        /**
         * Ми маємо створити Fragment лише один раз, відповідно якщо ми будемо просто стоврювати його в onCreate то при перевороті
         * екрану у нас буде створено два фрагмента, один сворить система, інший ми.Тож ми можемо додати перевірку, якщо savedInstanceState == null
         * то Ativity не пересвторювалась
         */
        if (savedInstanceState == null) {
            launchRightMode()
        }
    }

    private fun parseIntent() {
        intentParser.intent = intent

        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("param shop item id not found")
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun launchRightMode() {
        val fragment = when (screenMode) {
            MODE_ADD -> ShopItemFragment.newInstanceAddItem()
            MODE_EDIT -> ShopItemFragment.newInstanceEditItem(shopItemId)
            else -> throw RuntimeException("Unknown screen mode $screenMode")
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .commit() // запустить транзакцію на виконання
    }


    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"

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

    override fun onEditingFinished() {
        finish()
    }
}