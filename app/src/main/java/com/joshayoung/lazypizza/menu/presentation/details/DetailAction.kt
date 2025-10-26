package com.joshayoung.lazypizza.menu.presentation.details

import com.joshayoung.lazypizza.menu.presentation.models.ProductUi
import com.joshayoung.lazypizza.menu.presentation.models.ToppingUi
import java.math.BigDecimal

sealed interface DetailAction {
    data class IncrementPrice(
        var price: BigDecimal
    ) : DetailAction

    data class DecrementPrice(
        var price: BigDecimal
    ) : DetailAction

    data class AddItemToCart(
        var productUi: ProductUi?
    ) : DetailAction

    data class AddTopping(
        var toppingUi: ToppingUi
    ) : DetailAction

    data class RemoveTopping(
        var toppingUi: ToppingUi
    ) : DetailAction
}