package com.example.shoppinglist

import android.app.Application
import com.example.shoppinglist.di.DependencyProvider

class ShopListApp : Application() {

    override fun onCreate() {
        super.onCreate()
        DependencyProvider.provideContext(this)
    }
}