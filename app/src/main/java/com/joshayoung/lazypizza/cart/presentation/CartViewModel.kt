package com.joshayoung.lazypizza.cart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.core.data.database.CartDao
import com.joshayoung.lazypizza.core.domain.CartRepository
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
        _state.update {
            it.copy(
                isLoadingCart = true
            )
        }
        viewModelScope.launch {
            val productUiItems =
                cartRepository.productsInCart().map { item ->
                    item.toProductUi()
                }
            _state.update {
                it.copy(
                    items = productUiItems,
                    isLoadingCart = false
                )
            }
        }
    }

    fun onAction(action: CartAction) {
    }
}