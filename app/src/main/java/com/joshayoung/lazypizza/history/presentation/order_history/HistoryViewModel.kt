package com.joshayoung.lazypizza.history.presentation.order_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.core.domain.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(
    cartRepository: CartRepository
) : ViewModel() {
    private var _state = MutableStateFlow(HistoryState())

    val cartCount: Flow<Int> = cartRepository.getNumberProductsInCart(1)

    val state =
        _state.onStart { }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(1000L),
            HistoryState()
        )

    fun onAction(action: HistoryAction) {
    }
}