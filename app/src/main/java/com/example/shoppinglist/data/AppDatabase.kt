package com.example.shoppinglist.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ShopItemDbModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun shopListDao(): ShopListDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "shop_item.db"

        fun getInstance(application: Application): AppDatabase {
            INSTANCE?.let {
                return it
            }
            /**
             * Якщо два потоки одночасно викликали метод getInstance, і в обох була виконана перевірка і виявилось,
             * INSTANCE == null, то обидва потоки дійдуть до synchronized і якийсь першим зайде в цей блок, а інший
             * буде чекати. Якщо ми не зробимо цю перевірку то ми можемо INSTANCE дати нове значення, і коли він вийде,
             * то інший поток зайде і дасть INSTANCE нове значення. Тому в блоці synchronized потрібно перевіряти що
             * INSTANCE != null
             */
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = db
                return db
            }
        }
    }
}