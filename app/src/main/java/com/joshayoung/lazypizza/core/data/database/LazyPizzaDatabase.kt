package com.joshayoung.lazypizza.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joshayoung.lazypizza.core.data.database.entity.CartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductsInCartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingsInCartEntity
import com.joshayoung.lazypizza.order.data.database.OrderDao
import com.joshayoung.lazypizza.order.data.database.entity.OrderEntity

@Database(
    entities = [
        CartEntity::class, ProductEntity::class, ProductsInCartEntity::class, ToppingEntity::class,
        ToppingsInCartEntity::class, OrderEntity::class
    ],
    version = 1
)
abstract class LazyPizzaDatabase : RoomDatabase() {
    abstract val cardDao: CartDao
    abstract val productDao: ProductDao

    abstract val toppingDao: ToppingDao

    abstract val orderDao: OrderDao

    companion object {
        const val DATABASE_NAME = "lazy_pizza_db"
    }
}