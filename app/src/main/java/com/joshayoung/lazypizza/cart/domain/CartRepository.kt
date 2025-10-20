package com.joshayoung.lazypizza.cart.domain

import com.joshayoung.lazypizza.core.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun addToCart(item: Int)

    fun getCartData(): Flow<String?>

    suspend fun removeFromCart()

    suspend fun getTableData(table: String): List<Product>

    suspend fun getProduct(productId: String?): Product?
}