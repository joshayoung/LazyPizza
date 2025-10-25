package com.joshayoung.lazypizza.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joshayoung.lazypizza.core.domain.models.CartEntity
import com.joshayoung.lazypizza.core.domain.models.CartProductId
import com.joshayoung.lazypizza.core.domain.models.ProductEntity
import com.joshayoung.lazypizza.core.domain.models.ToppingEntity

@Database(
    entities = [CartEntity::class, ProductEntity::class, CartProductId::class, ToppingEntity::class],
    version = 1
)
abstract class CartDatabase : RoomDatabase() {
    abstract val cardDao: CartDao

    companion object {
        const val DATABASE_NAME = "cart_db"
    }
}