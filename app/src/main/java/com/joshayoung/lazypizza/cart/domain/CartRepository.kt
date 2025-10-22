package com.joshayoung.lazypizza.cart.domain

import com.joshayoung.lazypizza.core.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun addToCart(product: Product)

    fun getCartData(): Flow<List<Product>?>

    suspend fun removeFromCart(product: Product)

    suspend fun getProducts(table: String): List<Product>

    suspend fun getProduct(productId: String?): Product?
}