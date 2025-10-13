package com.joshayoung.lazypizza.core.domain

import com.joshayoung.lazypizza.core.domain.models.Product

interface LazyPizzaRepository {
    suspend fun login(
        email: String,
        password: String
    ): Boolean

    suspend fun getTableData(table: String): List<Product>

    suspend fun getData(productId: String?): Product?
}