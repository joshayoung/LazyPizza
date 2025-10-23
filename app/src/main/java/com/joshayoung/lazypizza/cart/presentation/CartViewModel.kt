package com.joshayoung.lazypizza.cart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.cart.domain.CartRepository
import com.joshayoung.lazypizza.core.presentation.mappers.toProductUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(
    private var cartRepository: CartRepository
) : ViewModel() {
    private var _state = MutableStateFlow(CartState())

    val state =
        _state.onStart { loadCart() }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(1000L),
            CartState()
        )

    private fun loadCart() {
        viewModelScope.launch {
            cartRepository.getProducts().collectLatest { data ->
                val all = data.map { it.toProductUi() }
                _state.update {
                    it.copy(
                        items = all
                    )
                }
            }
        }
    }

    fun onAction(action: CartAction) {
    }
}