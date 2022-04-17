package com.example.shoppinglist.di

import android.app.Application
import android.content.Context
import com.example.shoppinglist.presentation.modify_item.ShopItemFragment
import com.example.shoppinglist.presentation.shop_list.MainActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [ViewModelModule::class, DataModule::class])
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)

    fun inject(shopItemFragment: ShopItemFragment)

    @Component.Factory
    interface ApplicationFactory {
        fun create(
            @BindsInstance context: Application
        ): ApplicationComponent
    }
}