package com.joshayoung.lazypizza.core.presentation.utils

import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.core.presentation.models.BottomNavItem
import com.joshayoung.lazypizza.menu.data.models.Products
import com.joshayoung.lazypizza.menu.presentation.models.ProductType
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi
import java.math.BigDecimal

val previewBottomNavItems =
    listOf(
        BottomNavItem(
            label = "Menu",
            selected = true,
            clickAction = {
            },
            imageResource = R.drawable.book
        ),
        BottomNavItem(
            label = "Cart",
            selected = false,
            clickAction = {
            },
            imageResource = R.drawable.cart
        ),
        BottomNavItem(
            label = "History",
            selected = false,
            clickAction = {
            },
            imageResource = R.drawable.history
        )
    )

val previewProducts =
    listOf(
        Products(
            name = "Pizzas",
            items =
                listOf(
                    ProductUi(
                        id = "1",
                        description = "A delicious food",
                        imageResource = R.drawable.hawaiian,
                        name = "Hawaiian Pizza",
                        price = BigDecimal("10.19"),
                        type = ProductType.ENTRE
                    ),
                    ProductUi(
                        id = "2",
                        description =
                            "Tomato sauce, mozzarella, " +
                                "mushrooms, olives, bell pepper, onion, corn",
                        imageResource = R.drawable.meat_lovers,
                        name = "Veggie Delight",
                        price = BigDecimal("9.79"),
                        type = ProductType.ENTRE
                    )
                )
        ),
        Products(
            name = "Ice Cream",
            items =
                listOf(
                    ProductUi(
                        id = "3",
                        description = "A delicious food",
                        imageResource = R.drawable.cookies,
                        name = "Hawaiian Pizza",
                        price = BigDecimal("10.19")
                    ),
                    ProductUi(
                        id = "4",
                        description = "Another food",
                        imageResource = R.drawable.strawberry,
                        name = "Meat Lovers Pizza",
                        price = BigDecimal("13.28")
                    )
                )
        ),
        Products(
            name = "Drinks",
            items =
                listOf(
                    ProductUi(
                        id = "5",
                        description = "A delicious food",
                        imageResource = R.drawable.mineral_water,
                        name = "Hawaiian Pizza",
                        price = BigDecimal("8.18")
                    ),
                    ProductUi(
                        id = "6",
                        description = "Another food",
                        imageResource = R.drawable.pepsi,
                        name = "Meat Lovers Pizza",
                        price = BigDecimal("18.88")
                    )
                )
        ),
        Products(
            name = "Sauces",
            items =
                listOf(
                    ProductUi(
                        id = "7",
                        description = "A delicious food",
                        imageResource = R.drawable.spicy_chili_sauce,
                        name = "Hawaiian Pizza",
                        price = BigDecimal("21.19")
                    ),
                    ProductUi(
                        id = "8",
                        description = "Another food",
                        imageResource = R.drawable.bbq_sauce,
                        name = "Meat Lovers Pizza",
                        price = BigDecimal("5.43")
                    )
                )
        )
    )
