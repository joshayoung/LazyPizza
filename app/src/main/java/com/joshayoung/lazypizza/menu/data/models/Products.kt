package com.joshayoung.lazypizza.menu.data.models

import com.joshayoung.lazypizza.menu.presentation.models.ProductUi

data class Products(
    val name: String,
    // TODO: This should be a Product:
    val items: List<ProductUi>
)
