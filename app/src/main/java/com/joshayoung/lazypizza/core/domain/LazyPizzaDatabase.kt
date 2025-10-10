package com.joshayoung.lazypizza.core.domain

import com.joshayoung.lazypizza.core.domain.models.Product

interface LazyPizzaDatabase {
    suspend fun getTableData(table: String): List<Product>
}