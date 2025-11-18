package com.joshayoung.lazypizza.cart.presentation.checkout

import com.joshayoung.lazypizza.core.presentation.models.InCartItemUi
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi
import java.math.BigDecimal

data class CheckoutState(
    val items: List<InCartItemUi> = emptyList(),
    val isLoadingCart: Boolean = true,
    val cartItems: Int = 0,
    val recommendedAddOns: List<ProductUi> = emptyList(),
    val checkoutPrice: BigDecimal = BigDecimal(0.0),
    val earliestTime: Boolean = true,
    val scheduleDate: Boolean = false,
    val scheduleTime: Boolean = false,
    val timeScheduled: Boolean = false,
    val timeError: String = "",
    val pickupTime: String = "00:00",
    val date: Long? = null,
    val hour: Int? = null,
    val minute: Int? = null,
    val orderInProgress: Boolean = false
)
