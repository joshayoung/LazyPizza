package com.joshayoung.lazypizza.cart.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joshayoung.lazypizza.cart.domain.models.CartEntity

@Database(entities = [CartEntity::class], version = 1)
abstract class CartDatabase : RoomDatabase() {
    abstract val cardDao: CartDao

    companion object {
        const val DATABASE_NAME = "cart_db"
    }
}