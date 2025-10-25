package com.joshayoung.lazypizza.menu.presentation.details

import com.joshayoung.lazypizza.menu.presentation.models.ProductUi
import com.joshayoung.lazypizza.menu.presentation.models.ToppingUi
import java.math.BigDecimal

data class DetailsState(
    val toppings: List<ToppingUi> = emptyList(),
    val productUi: ProductUi? = null,
    val totalPrice: BigDecimal = BigDecimal(0.0)
)
