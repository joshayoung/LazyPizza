package com.joshayoung.lazypizza.menu.data

import com.joshayoung.lazypizza.core.data.database.dto.ProductInCartDto
import com.joshayoung.lazypizza.core.presentation.models.InCartItemUi
import com.joshayoung.lazypizza.core.presentation.utils.getMenuTypeEnum
import com.joshayoung.lazypizza.menu.presentation.models.MenuTypeUi

fun List<ProductInCartDto>.toInCartItemUi(
    toppingsForDisplay: Map<String, Int> = mapOf()
): InCartItemUi {
    val lineNumbers = this.mapNotNull { it.lineItemId }
    return InCartItemUi(
        lineNumbers = lineNumbers,
        name = this.first().name,
        description = this.first().description,
        imageResource = this.first().imageResource,
        toppingsForDisplay = toppingsForDisplay,
        imageUrl = this.first().imageUrl,
        type = getMenuTypeEnum(this.first().type),
        price = this.first().price,
        remoteId = this.first().remoteId,
        productId = this.first().productId,
        numberInCart = lineNumbers.count()
    )
}