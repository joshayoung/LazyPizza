package com.joshayoung.lazypizza.menu.presentation.home

import com.joshayoung.lazypizza.core.domain.models.InCartItem

sealed interface HomeAction {
    data class AddItemToCart(
        var inCartItem: InCartItem
    ) : HomeAction

    data class RemoveItemFromCart(
        var inCartItem: InCartItem
    ) : HomeAction

    data class RemoveAllFromCart(
        var inCartItem: InCartItem
    ) : HomeAction
}