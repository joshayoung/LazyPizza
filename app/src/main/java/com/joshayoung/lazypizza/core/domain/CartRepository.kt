package com.joshayoung.lazypizza.core.domain

import com.joshayoung.lazypizza.core.domain.models.CartEntity
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.domain.models.ProductEntityWithCartStatus
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun addProductToCart(product: Product)

    fun getCart(): Flow<CartEntity>

    suspend fun removeProductFromCart(product: Product)

    suspend fun getProducts(): Flow<List<Product>>

    suspend fun updateLocalWithRemote(reload: Boolean = false)

    suspend fun getAllProducts(): List<Product>

    suspend fun createCartForUser(cartId: Long)

    suspend fun allProductsWithCartItems(): List<ProductEntityWithCartStatus>
}