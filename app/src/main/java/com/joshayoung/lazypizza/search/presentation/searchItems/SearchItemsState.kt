package com.joshayoung.lazypizza.search.presentation.searchItems

import androidx.compose.foundation.text.input.TextFieldState
import com.joshayoung.lazypizza.search.data.models.AllProducts

data class SearchItemsState(
    var search: TextFieldState = TextFieldState(),
    var items: List<AllProducts> = emptyList(),
    val token: String = "",
    val pizzaScrollPosition: Int = 0,
    val drinkScrollPosition: Int = 0,
    val sauceScrollPosition: Int = 0,
    val iceCreamScrollPosition: Int = 0
)
