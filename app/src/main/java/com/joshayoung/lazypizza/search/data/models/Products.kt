package com.joshayoung.lazypizza.search.data.models

import com.joshayoung.lazypizza.search.presentation.models.ProductUi

data class Products(
    val name: String,
    val items: List<ProductUi>
)
