package com.joshayoung.lazypizza.search.presentation.details

import com.joshayoung.lazypizza.search.ImageResource
import com.joshayoung.lazypizza.search.data.models.Product

data class DetailsState(
    val image: ImageResource? = null,
    val title: String? = null,
    val description: String? = null,
    val toppings: List<Product> = emptyList(),
    val token: String = ""
)
