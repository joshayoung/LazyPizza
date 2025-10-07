package com.joshayoung.lazypizza.search.presentation.searchItems

import androidx.compose.foundation.text.input.TextFieldState
import com.joshayoung.lazypizza.search.data.models.Product

data class SearchItemsState(
    var search: TextFieldState = TextFieldState(),
    var products: List<Product> = emptyList(),
    val token: String = ""
)
