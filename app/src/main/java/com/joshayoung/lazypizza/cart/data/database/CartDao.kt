package com.joshayoung.lazypizza.cart.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.joshayoung.lazypizza.cart.domain.models.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Upsert
    suspend fun upsertCartItem(cartItem: CartEntity)

    @Query("SELECT * FROM cart")
    fun getCartItems(): Flow<List<CartEntity>>
}