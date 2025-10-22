package com.joshayoung.lazypizza.cart.domain

import com.joshayoung.lazypizza.cart.domain.models.CartEntity
import com.joshayoung.lazypizza.core.networking.DataError
import com.joshayoung.lazypizza.core.networking.Result
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getCartItems(): Flow<List<CartEntity>>

    suspend fun upsertCart(note: CartEntity): Result<CartEntity, DataError.Local>
}