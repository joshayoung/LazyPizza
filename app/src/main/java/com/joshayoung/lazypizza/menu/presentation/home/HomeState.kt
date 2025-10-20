package com.joshayoung.lazypizza.menu.presentation.home

import androidx.compose.foundation.text.input.TextFieldState
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.menu.presentation.models.MenuType
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi

data class HomeState(
    var search: TextFieldState = TextFieldState(),
    var items: Map<MenuType, List<ProductUi>> = emptyMap(),
    val pizzaScrollPosition: Int = 0,
    val drinkScrollPosition: Int = 0,
    val sauceScrollPosition: Int = 0,
    val iceCreamScrollPosition: Int = 0,
    val noItemsFound: Boolean = false,
    val isLoadingProducts: Boolean = false,
    val cartItems: Int = 0
)
