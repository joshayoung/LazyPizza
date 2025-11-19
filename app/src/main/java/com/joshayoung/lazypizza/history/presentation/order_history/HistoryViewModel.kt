package com.joshayoung.lazypizza.history.presentation.order_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.core.domain.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val cardRepository: CartRepository
) : ViewModel() {
    private var _state = MutableStateFlow(HistoryState())

    init {
        // TODO: Use correct user here:
        viewModelScope.launch {
            val orders = cardRepository.getOrdersFor("userId")
            _state.update {
                it.copy(
                    orders = orders
                )
            }
        }
    }

    val state =
        _state.onStart { }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(1000L),
            HistoryState()
        )
}