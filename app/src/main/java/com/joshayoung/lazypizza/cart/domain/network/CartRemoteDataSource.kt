package com.joshayoung.lazypizza.cart.domain.network

import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.presentation.mappers.toProductEntity
import io.appwrite.services.TablesDB

interface CartRemoteDataSource {
    suspend fun getProducts(table: String): List<Product>

    suspend fun getProduct(productId: String?): Product?
}