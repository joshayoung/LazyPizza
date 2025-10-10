package com.joshayoung.lazypizza.search.presentation.details

import com.joshayoung.lazypizza.search.data.models.Product

data class DetailsState(
    val toppings: List<Product> = emptyList(),
    val token: String = "",
    val product: Product? = null
)
