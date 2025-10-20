package com.joshayoung.lazypizza.core.presentation.mappers

import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi
import java.math.BigDecimal
import java.math.RoundingMode

fun Product.toProductUi(): ProductUi =
    ProductUi(
        id = id,
        description = description,
        imageUrl = imageUrl,
        imageResource = imageResource,
        name = name,
        price = BigDecimal(price).setScale(2, RoundingMode.HALF_UP)
    )