package com.joshayoung.lazypizza.core.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.joshayoung.lazypizza.core.domain.models.CartEntity
import com.joshayoung.lazypizza.core.domain.models.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Upsert
    suspend fun upsertCartItem(cartItem: CartEntity)

    @Upsert
    suspend fun upsertProduct(productEntity: ProductEntity)

    @Query("SELECT * FROM cart")
    fun getCartItems(): Flow<List<CartEntity>>

    @Query("SELECT * FROM product")
    fun getProduct(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM product")
    suspend fun getAllProducts(): List<ProductEntity>
}