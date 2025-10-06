package com.joshayoung.lazypizza.search.presentation.searchItems

import androidx.compose.foundation.text.input.TextFieldState
import com.joshayoung.lazypizza.search.ImageResource

data class SearchItemsState(
    var images: List<ImageResource>,
    var search : TextFieldState = TextFieldState()
)
