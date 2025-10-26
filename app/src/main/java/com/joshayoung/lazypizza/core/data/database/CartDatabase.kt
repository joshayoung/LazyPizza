package com.joshayoung.lazypizza.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joshayoung.lazypizza.core.data.database.entity.CartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductsInCart
import com.joshayoung.lazypizza.core.data.database.entity.ToppingEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingsInCart

@Database(
    entities = [
        CartEntity::class, ProductEntity::class, ProductsInCart::class, ToppingEntity::class,
        ToppingsInCart::class
    ],
    version = 1
)
abstract class CartDatabase : RoomDatabase() {
    abstract val cardDao: CartDao

    companion object {
        const val DATABASE_NAME = "cart_db"
    }
}