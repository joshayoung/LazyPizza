package com.joshayoung.lazypizza.menu.presentation.home

import androidx.compose.foundation.text.input.TextFieldState
import com.joshayoung.lazypizza.menu.presentation.models.MenuItemUi

data class HomeState(
    var search: TextFieldState = TextFieldState(),
    var items: List<MenuItemUi> = emptyList(),
    val noItemsFound: Boolean = false,
    val isLoadingProducts: Boolean = false,
    val cartItems: Int = 0
)
