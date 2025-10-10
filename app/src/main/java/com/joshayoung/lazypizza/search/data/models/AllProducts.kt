package com.joshayoung.lazypizza.search.data.models

import com.joshayoung.lazypizza.core.domain.models.Product

data class AllProducts(
    val name: String,
    val items: List<Product>
)
