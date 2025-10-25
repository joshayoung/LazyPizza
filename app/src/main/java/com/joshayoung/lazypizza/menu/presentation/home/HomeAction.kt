package com.joshayoung.lazypizza.menu.presentation.home

import com.joshayoung.lazypizza.menu.presentation.models.ProductUi

sealed interface HomeAction {
    data class AddItemToCart(
        var productUi: ProductUi
    ) : HomeAction

    data class RemoveItemFromCart(
        var productUi: ProductUi
    ) : HomeAction

    data class RemoveAllFromCart(
        var productUi: ProductUi
    ) : HomeAction
}