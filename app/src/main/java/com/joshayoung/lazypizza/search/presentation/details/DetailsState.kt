package com.joshayoung.lazypizza.search.presentation.details

import com.joshayoung.lazypizza.search.presentation.models.ProductUi
import java.math.BigDecimal

data class DetailsState(
    val toppings: List<ProductUi> = emptyList(),
    val productUi: ProductUi? = null,
    val totalPrice: BigDecimal = BigDecimal(0.0)
)
