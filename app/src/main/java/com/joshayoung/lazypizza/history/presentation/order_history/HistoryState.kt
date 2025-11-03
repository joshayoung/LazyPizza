package com.joshayoung.lazypizza.history.presentation.order_history

import com.joshayoung.lazypizza.history.domain.models.Order

data class HistoryState(
    val cartItems: Int = 0,
    val orders: List<Order> = emptyList(),
    val isSignedIn: Boolean = false
)
