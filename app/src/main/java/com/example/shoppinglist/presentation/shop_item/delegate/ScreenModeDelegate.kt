package com.example.shoppinglist.presentation.shop_item.delegate

import android.content.Intent
import com.example.shoppinglist.presentation.shop_item.ShopItemActivity
import kotlin.properties.Delegates.notNull
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ScreenModeDelegate : ReadOnlyProperty<ShopItemActivity, String> {

    private var screenMode = MODE_UNKNOWN
    private var isExtracted = false
    var intent by notNull<Intent>()

    override fun getValue(thisRef: ShopItemActivity, property: KProperty<*>): String {
        if (isExtracted.not()){
            extractIntent()
            isExtracted = true
        }
        return screenMode
    }

    private fun extractIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("unknown param screen mode $mode")
        }
        screenMode = mode
    }

    companion object {
        private const val MODE_UNKNOWN = ""
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        internal const val MODE_EDIT = "mode_edit"
        internal const val MODE_ADD = "mode_add"
    }
}