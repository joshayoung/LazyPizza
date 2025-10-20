package com.joshayoung.lazypizza.menu.presentation.home

import androidx.compose.foundation.text.input.TextFieldState
import com.joshayoung.lazypizza.menu.presentation.models.MenuItemUi

data class HomeState(
    var search: TextFieldState = TextFieldState(),
    var items: List<MenuItemUi> = emptyList(),
    val pizzaScrollPosition: Int = 0,
    val drinkScrollPosition: Int = 0,
    val sauceScrollPosition: Int = 0,
    val iceCreamScrollPosition: Int = 0,
    val noItemsFound: Boolean = false,
    val isLoadingProducts: Boolean = false,
    val cartItems: Int = 0
)
