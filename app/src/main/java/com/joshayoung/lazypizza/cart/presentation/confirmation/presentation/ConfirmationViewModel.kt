package com.joshayoung.lazypizza.cart.presentation.confirmation.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.order.domain.OrderRepository
import kotlinx.coroutines.launch

class ConfirmationViewModel(
    savedStateHandle: SavedStateHandle,
    private val orderRepository: OrderRepository
) : ViewModel() {
    var state by mutableStateOf(ConfirmationState())
        private set

    init {
        viewModelScope.launch {
            state =
                state.copy(
                    isLoading = true
                )
            savedStateHandle.get<String>("orderNumber")?.let { orderNumber ->

                val result = orderRepository.getOrderInfo(orderNumber)

                result?.let { orderDto ->
                    state =
                        state.copy(
                            orderNumber = result.number,
                            pickupTime = result.pickupTime
                        )
                }
                state =
                    state.copy(
                        isLoading = false
                    )
            }
        }
    }
}