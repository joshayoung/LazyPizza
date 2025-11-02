package com.joshayoung.lazypizza.core.presentation.mappers

import com.joshayoung.lazypizza.core.data.database.entity.ProductEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingEntity
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.domain.models.Topping
import com.joshayoung.lazypizza.core.presentation.utils.getMenuTypeEnum
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi
import com.joshayoung.lazypizza.menu.presentation.models.ToppingUi
import java.math.BigDecimal
import java.math.RoundingMode

fun Product.toProductUi(
    inCart: Boolean = false,
    numberInCart: Int = 0
): ProductUi =
    ProductUi(
        id = id,
        localId = localId,
        description = description,
        remoteImageUrl = imageUrl,
        imageResource = imageResource,
        name = name,
        price = BigDecimal(price).setScale(2, RoundingMode.HALF_UP),
        type = getMenuTypeEnum(type),
        inCart = inCart,
        numberInCart = numberInCart
    )

fun Topping.toToppingUi(): ToppingUi {
    return ToppingUi(
        localId = localId,
        remoteId = remoteId,
        name = name,
        price = BigDecimal(price),
        imageUrl = imageUrl
    )
}

fun Product.toProductEntity(): ProductEntity =
    ProductEntity(
        remoteId = id,
        description = description,
        imageUrl = imageUrl,
        name = name,
        price = price,
        type = type
    )

fun Topping.toToppingEntity(): ToppingEntity {
    return ToppingEntity(
        remoteId = remoteId,
        imageUrl = imageUrl,
        name = name,
        price = price
    )
}

// TODO: Double check these values:
fun ProductEntity.toProduct(): Product =
    Product(
        description = description,
        imageUrl = imageUrl,
        name = name,
        price = price,
        type = type,
        id = remoteId,
        localId = productId
    )

fun ToppingEntity.toTopping(): Topping {
    return Topping(
        localId = toppingId,
        remoteId = remoteId,
        name = name,
        price = price,
        imageUrl = imageUrl
    )
}

fun ProductUi.toProduct(): Product =
    Product(
        id = id,
        localId = localId,
        lineItemId = lineItemId,
        description = description,
        imageUrl = remoteImageUrl,
        imageResource = imageResource,
        name = name,
        // TODO: Is this correct?
        price = price.toString(),
        // TODO: Is this correct?
        type = type?.name ?: ""
    )
