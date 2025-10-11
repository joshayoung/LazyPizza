package com.joshayoung.lazypizza.search.presentation.details

import com.joshayoung.lazypizza.core.domain.models.Product

data class DetailsState(
    val toppings: List<Product> = emptyList(),
    val product: Product? = null
)
