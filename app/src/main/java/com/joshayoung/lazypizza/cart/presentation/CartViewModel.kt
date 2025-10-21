package com.joshayoung.lazypizza.cart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class CartViewModel : ViewModel() {
    private var _state = MutableStateFlow(CartState())

    val state =
        _state.onStart { loadCart() }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(1000L),
            CartState()
        )

    private fun loadCart() {
    }

    fun onAction(action: CartAction) {
    }
}