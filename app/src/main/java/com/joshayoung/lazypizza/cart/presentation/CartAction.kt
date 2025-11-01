package com.joshayoung.lazypizza.cart.presentation

import com.joshayoung.lazypizza.core.presentation.models.InCartItemUi
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi

sealed interface CartAction {
    data class AddItemToCart(
        var inCartItemUi: InCartItemUi
    ) : CartAction

    data class RemoveItemFromCart(
        var inCartItemUi: InCartItemUi
    ) : CartAction

    data class RemoveAllFromCart(
        var inCartItemUi: InCartItemUi
    ) : CartAction

    data class AddAddOnToCart(
        var productUi: ProductUi
    ) : CartAction
}
