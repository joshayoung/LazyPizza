package com.joshayoung.lazypizza.menu.presentation.home

sealed interface HomeAction {
    data class AddItemToCart(
        var item: Int
    ) : HomeAction

    data class RemoveItemFromCart(
        var item: Int
    ) : HomeAction
}