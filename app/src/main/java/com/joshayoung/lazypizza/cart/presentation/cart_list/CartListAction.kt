package com.joshayoung.lazypizza.cart.presentation.cart_list

import com.joshayoung.lazypizza.core.presentation.models.InCartItemUi
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi

sealed interface CartListAction {
    data class AddItemToCartList(
        var inCartItemUi: InCartItemUi
    ) : CartListAction

    data class RemoveItemFromCartList(
        var inCartItemUi: InCartItemUi
    ) : CartListAction

    data class RemoveAllFromCartList(
        var inCartItemUi: InCartItemUi
    ) : CartListAction

    data class AddAddOnToCartList(
        var productUi: ProductUi
    ) : CartListAction
}
