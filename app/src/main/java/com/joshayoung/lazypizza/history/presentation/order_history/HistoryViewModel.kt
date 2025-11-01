package com.joshayoung.lazypizza.history.presentation.order_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.cart.presentation.cart_list.CartState
import com.joshayoung.lazypizza.core.domain.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {
    private var _state = MutableStateFlow(HistoryState())

    val state =
        _state.onStart { }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(1000L),
            HistoryState()
        )

    init {
        viewModelScope.launch {
            cartRepository.getNumberProductsInCart(1).collectLatest { count ->
                _state.update {
                    it.copy(
                        cartItems = count
                    )
                }
            }
        }
    }

    fun onAction(action: HistoryAction) {
    }
}