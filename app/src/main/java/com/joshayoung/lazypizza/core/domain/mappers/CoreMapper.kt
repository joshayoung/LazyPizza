package com.joshayoung.lazypizza.core.domain.mappers

import com.joshayoung.lazypizza.core.data.network.models.ProductInCartDto
import com.joshayoung.lazypizza.core.data.network.models.ToppingInCartDto
import com.joshayoung.lazypizza.core.domain.models.ProductInCart
import com.joshayoung.lazypizza.core.domain.models.ToppingInCart

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

fun ToppingInCartDto.toToppingInCart(): ToppingInCart {
    return ToppingInCart(
        toppingId = toppingId,
        remoteId = remoteId,
        name = name,
        price = price,
        imageUrl = imageUrl,
        productId = productId
    )
}