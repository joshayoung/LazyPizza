package com.joshayoung.lazypizza.cart.presentation.checkout

import com.joshayoung.lazypizza.cart.presentation.cart_list.CartAction
import com.joshayoung.lazypizza.core.presentation.models.InCartItemUi
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi

sealed interface CheckoutAction {
    data class AddItemToCart(
        var inCartItemUi: InCartItemUi
    ) : CheckoutAction

    data class RemoveItemFromCart(
        var inCartItemUi: InCartItemUi
    ) : CheckoutAction

    data class RemoveAllFromCart(
        var inCartItemUi: InCartItemUi
    ) : CheckoutAction

    data class AddAddOnToCart(
        var productUi: ProductUi
    ) : CheckoutAction
}