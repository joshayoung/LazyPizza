package com.joshayoung.lazypizza.core.presentation.mappers

import com.joshayoung.lazypizza.core.data.database.dto.ProductWithCartStatusDto
import com.joshayoung.lazypizza.core.data.database.entity.ProductEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingEntity
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.domain.models.Topping
import com.joshayoung.lazypizza.menu.presentation.models.MenuTypeUi
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

fun ProductWithCartStatusDto.toProductUi(): ProductUi {
    return ProductUi(
        localId = productId,
        id = remoteId,
        remoteImageUrl = imageUrl,
        description = description,
        imageResource = imageResource,
        name = name,
        price = BigDecimal(price).setScale(2, RoundingMode.HALF_UP),
        type = getMenuTypeEnum(type),
        inCart = (numberInCart ?: 0) > 0,
        numberInCart = numberInCart
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

private fun getMenuTypeEnum(menuType: String): MenuTypeUi {
    when (menuType) {
        "entree" -> {
            return MenuTypeUi.Entree
        }
        "dessert" -> {
            return MenuTypeUi.Dessert
        }
        "beverage" -> {
            return MenuTypeUi.Beverage
        }
        "sauce" -> {
            return MenuTypeUi.Sauce
        }
        else -> {
            return MenuTypeUi.Unknown
        }
    }
}