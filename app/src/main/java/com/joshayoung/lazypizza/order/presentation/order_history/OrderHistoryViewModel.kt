package com.joshayoung.lazypizza.order.presentation.order_history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.core.presentation.FirebaseAuthUiClient
import com.joshayoung.lazypizza.order.domain.OrderProcessor
import com.joshayoung.lazypizza.order.presentation.mappers.toOrderUi
import kotlinx.coroutines.launch

class OrderHistoryViewModel(
    private val orderProcessor: OrderProcessor
) : ViewModel() {
    var state by mutableStateOf(OrderHistoryState())
        private set

    private val firebaseAuthUiClient: FirebaseAuthUiClient = FirebaseAuthUiClient()

    init {
        viewModelScope.launch {
            state =
                state.copy(
                    loadingOrders = true
                )
            val user = firebaseAuthUiClient.currentUser
            val orders = orderProcessor.getOrdersFor(user).map { order -> order.toOrderUi() }
            state =
                state.copy(
                    orderUis = orders,
                    loadingOrders = false
                )
        }
    }
}