package com.joshayoung.lazypizza.core

import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.search.presentation.models.ProductUi

fun Product.toProductUi(): ProductUi =
    ProductUi(
        description = description,
        imageUrl = imageUrl,
        imageResource = imageResource,
        name = name,
        price = price
    )