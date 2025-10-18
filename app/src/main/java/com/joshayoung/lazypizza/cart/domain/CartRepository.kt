package com.joshayoung.lazypizza.cart.domain

import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun addItemToCart(item: Int)

    fun getCartData(): Flow<String?>

    suspend fun removeFromCart()
}