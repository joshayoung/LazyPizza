package com.joshayoung.lazypizza.history.presentation.order_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.presentation.FirebaseAuthUiClient
import com.joshayoung.lazypizza.history.presentation.mappers.toOrderUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val cardRepository: CartRepository
) : ViewModel() {
    private var _state = MutableStateFlow(HistoryState())

    // TODO: Should this be injected. I am using it in multiple places:
    private val firebaseAuthUiClient: FirebaseAuthUiClient = FirebaseAuthUiClient()

    init {
        // TODO: Use correct user here:
        viewModelScope.launch {
            val user = firebaseAuthUiClient.currentUser
            val orders = cardRepository.getOrdersFor(user).map { order -> order.toOrderUi() }
            _state.update {
                it.copy(
                    orderUis = orders
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