package com.joshayoung.lazypizza.cart.presentation

import com.joshayoung.lazypizza.core.domain.models.InCartItem
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi
import java.math.BigDecimal

data class CartState(
    val items: List<InCartItem> = emptyList(),
    val isLoadingCart: Boolean = true,
    val cartItems: Int = 0,
    val recommendedAddOns: List<ProductUi> = emptyList(),
    val checkoutPrice: BigDecimal = BigDecimal(0.0)
)
