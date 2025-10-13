package com.joshayoung.lazypizza.core

import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.search.presentation.models.ProductUi
import java.math.BigDecimal
import java.math.RoundingMode

fun Product.toProductUi(): ProductUi =
    ProductUi(
        description = description,
        imageUrl = imageUrl,
        imageResource = imageResource,
        name = name,
        price = BigDecimal(price).setScale(2, RoundingMode.HALF_UP)
    )