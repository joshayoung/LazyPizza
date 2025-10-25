package com.joshayoung.lazypizza.cart.presentation

import com.joshayoung.lazypizza.menu.presentation.home.HomeAction
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi

sealed interface CartAction {
    data class AddItemToCart(
        var productUi: ProductUi
    ) : CartAction

    data class RemoveItemFromCart(
        var productUi: ProductUi
    ) : CartAction

    data class RemoveAllFromCart(
        var productUi: ProductUi
    ) : CartAction
}
