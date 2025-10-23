package com.joshayoung.lazypizza.core.domain

import com.joshayoung.lazypizza.core.domain.models.CartEntity
import com.joshayoung.lazypizza.core.domain.models.ProductEntity
import com.joshayoung.lazypizza.core.networking.DataError
import com.joshayoung.lazypizza.core.networking.Result
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getCartItems(): Flow<List<CartEntity>>

    fun getProducts(): Flow<List<ProductEntity>>

    suspend fun upsertCart(cartEntity: CartEntity): Result<CartEntity, DataError.Local>

    suspend fun upsertProduct(productEntity: ProductEntity): Result<ProductEntity, DataError.Local>
}