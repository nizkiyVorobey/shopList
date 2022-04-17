package com.example.shoppinglist.di

import android.app.Application
import com.example.shoppinglist.data.AppDatabase
import com.example.shoppinglist.data.ShopListDao
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.ShopListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindShopListRepository(impl: ShopListRepositoryImpl): ShopListRepository

    companion object {
        @Provides
        @ApplicationScope
        fun providesShopListDao(
            context: Application,
        ): ShopListDao {
            return AppDatabase.getInstance(context).shopListDao()
        }

    }
}