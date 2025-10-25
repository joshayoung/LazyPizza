package com.joshayoung.lazypizza.cart.presentation

import com.joshayoung.lazypizza.menu.presentation.models.ProductUi

data class CartState(
    val items: List<ProductUi> = emptyList(),
    val isLoadingCart: Boolean = false
)
