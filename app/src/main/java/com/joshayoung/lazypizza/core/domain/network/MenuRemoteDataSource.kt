package com.joshayoung.lazypizza.core.domain.network

import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.domain.models.Topping

interface MenuRemoteDataSource {
    suspend fun getProducts(table: String): List<Product>

    suspend fun getToppings(table: String): List<Topping>

    suspend fun getProduct(productId: String?): Product?
}