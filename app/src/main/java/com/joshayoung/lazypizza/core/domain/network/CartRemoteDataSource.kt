package com.joshayoung.lazypizza.core.domain.network

import com.joshayoung.lazypizza.cart.domain.models.OrderDto
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.domain.models.Topping

interface CartRemoteDataSource {
    suspend fun getProducts(table: String): List<Product>

    suspend fun getToppings(table: String): List<Topping>

    suspend fun getProduct(productId: String?): Product?

    suspend fun placeOrder(): String?

    suspend fun getOrderInfo(id: String): OrderDto?
}