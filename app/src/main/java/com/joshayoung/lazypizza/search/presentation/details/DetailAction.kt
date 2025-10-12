package com.joshayoung.lazypizza.search.presentation.details

sealed interface DetailAction {
    data class IncrementPrice(
        var price: String
    ) : DetailAction

    data class DecrementPrice(
        var price: String
    ) : DetailAction
}