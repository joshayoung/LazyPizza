package com.joshayoung.lazypizza.search.domain.utils

import com.joshayoung.lazypizza.search.data.models.Product

interface LazyPizzaDatabase {
    suspend fun getTableData(table: String): List<Product>
}