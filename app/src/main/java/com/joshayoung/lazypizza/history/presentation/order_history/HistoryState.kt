package com.joshayoung.lazypizza.history.presentation.order_history

import com.joshayoung.lazypizza.cart.domain.models.OrderDto

data class HistoryState(
    val cartItems: Int = 0,
    val orders: List<OrderDto> = emptyList(),
    val isSignedIn: Boolean = false
)
