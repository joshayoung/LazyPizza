package com.joshayoung.lazypizza.menu.presentation.home

import com.joshayoung.lazypizza.core.presentation.models.InCartItemUi

sealed interface HomeAction {
    data class AddItemToCart(
        var inCartItemUi: InCartItemUi
    ) : HomeAction

    data class RemoveItemFromCart(
        var inCartItemUi: InCartItemUi
    ) : HomeAction

    data class RemoveAllFromCart(
        var inCartItemUi: InCartItemUi
    ) : HomeAction

    data object SignOut : HomeAction
}