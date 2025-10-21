package com.joshayoung.lazypizza.core.presentation.mappers

import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.menu.presentation.models.MenuType
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
        price = BigDecimal(price).setScale(2, RoundingMode.HALF_UP),
        type = getMenuTypeEnum(type)
    )

fun ProductUi.toProduct(): Product =
    Product(
        id = id,
        description = description,
        imageUrl = imageUrl,
        imageResource = imageResource,
        name = name,
        // TODO: Is this correct?
        price = price.toString(),
        // TODO: Is this correct?
        type = type?.name ?: ""
    )

private fun getMenuTypeEnum(menuType: String): MenuType {
    when (menuType) {
        "entree" -> {
            return MenuType.Entree
        }
        "dessert" -> {
            return MenuType.Dessert
        }
        "beverage" -> {
            return MenuType.Beverage
        }
        "sauce" -> {
            return MenuType.Sauce
        }
        else -> {
            return MenuType.Unknown
        }
    }
}