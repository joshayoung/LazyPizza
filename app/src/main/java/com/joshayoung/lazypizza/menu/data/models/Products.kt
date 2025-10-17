package com.joshayoung.lazypizza.menu.data.models

import com.joshayoung.lazypizza.menu.presentation.models.ProductUi

data class Products(
    val name: String,
    val items: List<ProductUi>
)
