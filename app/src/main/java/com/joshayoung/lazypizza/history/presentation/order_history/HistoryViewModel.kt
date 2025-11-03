package com.joshayoung.lazypizza.history.presentation.order_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.history.domain.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HistoryViewModel(
    private val historyRepository: HistoryRepository
) : ViewModel() {
    private var _state = MutableStateFlow(HistoryState())

    init {
        // TODO: Use correct user here:
        val orders = historyRepository.getOrdersFor("user")
        _state.update {
            it.copy(
                // TODO: Temporary for testing
                isSignedIn = true,
                orders = orders
            )
        }
    }

    val state =
        _state.onStart { }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(1000L),
            HistoryState()
        )

    fun onAction(action: HistoryAction) {
    }
}