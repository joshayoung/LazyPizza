package com.joshayoung.lazypizza.cart.presentation.cart_list

import com.joshayoung.lazypizza.core.presentation.models.InCartItemUi
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi
import java.math.BigDecimal

data class CartState(
    val items: List<InCartItemUi> = emptyList(),
    val isLoadingCart: Boolean = true,
    val cartItems: Int = 0,
    val recommendedAddOns: List<ProductUi> = emptyList(),
    val checkoutPrice: BigDecimal = BigDecimal(0.0)
)
