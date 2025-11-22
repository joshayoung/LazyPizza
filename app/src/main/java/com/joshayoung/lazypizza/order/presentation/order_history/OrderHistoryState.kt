package com.joshayoung.lazypizza.order.presentation.order_history

import com.joshayoung.lazypizza.order.presentation.models.OrderUi

data class OrderHistoryState(
    val cartItems: Int = 0,
    val orderUis: List<OrderUi> = emptyList(),
    val isSignedIn: Boolean = false
)
