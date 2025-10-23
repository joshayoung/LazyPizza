package com.joshayoung.lazypizza.cart.domain.network

import com.joshayoung.lazypizza.core.domain.models.Product

interface CartRemoteDataSource {
    suspend fun getProducts(table: String): List<Product>

    suspend fun getProduct(productId: String?): Product?
}