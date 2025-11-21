package com.joshayoung.lazypizza.cart.presentation.checkout

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

    data class SetDate(
        val date: Long?
    ) : CheckoutAction

    data class SetTime(
        val hour: Int,
        val minute: Int
    ) : CheckoutAction

    data object PickDateAndTime : CheckoutAction

    data object PlaceOrder : CheckoutAction

    data object CloseDatePicker : CheckoutAction

    data object CloseTimePicker : CheckoutAction

    data object PickEarliestTime : CheckoutAction
}