package com.joshayoung.lazypizza.cart.presentation.confirmation.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.core.domain.CartRepository
import kotlinx.coroutines.launch

class ConfirmationViewModel(
    savedStateHandle: SavedStateHandle,
    private val cartRepository: CartRepository
) : ViewModel() {
    var state by mutableStateOf(ConfirmationState())
        private set

    init {
        viewModelScope.launch {
            savedStateHandle.get<String>("orderNumber")?.let { orderNumber ->

                val result = cartRepository.getOrderInfo(orderNumber)

                result?.let { orderDto ->
                    state =
                        state.copy(
                            orderNumber = result.orderNumber,
                            pickupTime = result.pickupTime
                        )
                }

                println()
            }
        }
    }
}