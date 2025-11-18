package com.joshayoung.lazypizza.cart.presentation.confirmation.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.joshayoung.lazypizza.cart.data.di.Stuff
import com.joshayoung.lazypizza.core.utils.Routes
import kotlinx.coroutines.launch

class ConfirmationViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    init {
        viewModelScope.launch {
            savedStateHandle.get<String>("orderNumber")?.let { orderNumber ->

                println()
            }
        }
    }
}