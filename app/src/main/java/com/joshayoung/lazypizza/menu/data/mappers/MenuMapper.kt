package com.joshayoung.lazypizza.menu.data.mappers

import com.joshayoung.lazypizza.core.domain.models.ProductInCart
import com.joshayoung.lazypizza.core.domain.models.ToppingInCart
import com.joshayoung.lazypizza.core.presentation.models.InCartItemUi
import com.joshayoung.lazypizza.core.presentation.utils.getMenuTypeEnum

fun List<ProductInCart>.toInCartItemUi(
    key: Int = 0,
    toppings: List<ToppingInCart> = listOf(),
    toppingsForDisplay: Map<String, Int> = mapOf()
): InCartItemUi {
    val lineNumbers = this.mapNotNull { it.lineItemId }
    return InCartItemUi(
        lineNumbers = lineNumbers,
        key = key,
        name = this.first().name,
        description = this.first().description,
        imageResource = this.first().imageResource,
        toppingsForDisplay = toppingsForDisplay,
        toppings = toppings,
        imageUrl = this.first().imageUrl,
        type = getMenuTypeEnum(this.first().type),
        price = this.first().price,
        remoteId = this.first().remoteId,
        productId = this.first().productId,
        numberInCart = lineNumbers.count()
    )
}
