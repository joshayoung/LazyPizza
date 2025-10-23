package com.joshayoung.lazypizza.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joshayoung.lazypizza.core.domain.models.CartEntity
import com.joshayoung.lazypizza.core.domain.models.ProductEntity

@Database(entities = [CartEntity::class, ProductEntity::class], version = 1)
abstract class CartDatabase : RoomDatabase() {
    abstract val cardDao: CartDao

    companion object {
        const val DATABASE_NAME = "cart_db"
    }
}