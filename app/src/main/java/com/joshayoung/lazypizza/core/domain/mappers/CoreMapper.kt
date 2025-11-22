package com.joshayoung.lazypizza.core.domain.mappers

import com.joshayoung.lazypizza.core.data.network.models.ProductInCartDto
import com.joshayoung.lazypizza.core.domain.models.ProductInCart

fun ProductInCartDto.toProductInCart(): ProductInCart {
    return ProductInCart(
        lineItemId = lineItemId,
        remoteId = remoteId,
        productId = productId,
        nameWithToppingIds = nameWithToppingIds,
        name = name,
        price = price,
        description = description,
        imageUrl = imageUrl,
        imageResource = imageResource,
        type = type,
        numberInCart = numberInCart
    )
}