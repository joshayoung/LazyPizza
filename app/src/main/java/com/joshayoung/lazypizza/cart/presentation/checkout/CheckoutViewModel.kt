package com.joshayoung.lazypizza.cart.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class CheckoutViewModel : ViewModel() {
    private var _state = MutableStateFlow(CheckoutState())

    val state =
        _state
            .onStart {
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(1000L),
                CheckoutState()
            )

    fun onAction(action: CheckoutAction) {
    }
}