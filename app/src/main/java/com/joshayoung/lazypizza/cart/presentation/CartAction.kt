package com.joshayoung.lazypizza.cart.presentation

import com.joshayoung.lazypizza.core.presentation.models.InCartItem
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi

sealed interface CartAction {
    data class AddItemToCart(
        var inCartItem: InCartItem
    ) : CartAction

    data class RemoveItemFromCart(
        var inCartItem: InCartItem
    ) : CartAction

    data class RemoveAllFromCart(
        var inCartItem: InCartItem
    ) : CartAction

    data class AddAddOnToCart(
        var productUi: ProductUi
    ) : CartAction
}
