package com.joshayoung.lazypizza.search.presentation.details

import com.joshayoung.lazypizza.search.presentation.models.ProductUi

data class DetailsState(
    val toppings: List<ProductUi> = emptyList(),
    val productUi: ProductUi? = null
)
