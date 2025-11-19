package com.joshayoung.lazypizza.history.presentation.order_history

import com.joshayoung.lazypizza.history.presentation.models.OrderUi

data class HistoryState(
    val cartItems: Int = 0,
    val orderUis: List<OrderUi> = emptyList(),
    val isSignedIn: Boolean = false
)
