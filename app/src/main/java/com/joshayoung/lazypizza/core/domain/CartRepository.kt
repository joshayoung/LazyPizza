package com.joshayoung.lazypizza.core.domain

import com.joshayoung.lazypizza.core.domain.models.CartEntity
import com.joshayoung.lazypizza.core.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun addProductToCart(product: Product)

    fun getCart(): Flow<CartEntity>

    suspend fun removeProductFromCart(product: Product)

    suspend fun getProducts(): Flow<List<Product>>
}