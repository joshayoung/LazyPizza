package com.joshayoung.lazypizza.core.presentation.utils

import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.core.domain.models.InCartItem
import com.joshayoung.lazypizza.core.presentation.models.BottomNavItem
import com.joshayoung.lazypizza.menu.presentation.models.MenuItemUi
import com.joshayoung.lazypizza.menu.presentation.models.MenuType
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

val productUiListForPreview =
    listOf(
        ProductUi(
            id = "3",
            localId = 3,
            description = "A delicious food",
            imageResource = R.drawable.cookies,
            name = "Chocolate Ice Cream",
            price = BigDecimal("10.19"),
            inCart = true,
            numberInCart = 1
        ),
        ProductUi(
            id = "1",
            localId = 1,
            description = "A delicious food",
            imageResource = R.drawable.hawaiian,
            name = "Hawaiian Pizza",
            price = BigDecimal("10.19")
        ),
        ProductUi(
            id = "2",
            localId = 2,
            description =
                "Tomato sauce, mozzarella, " +
                    "mushrooms, olives, bell pepper, onion, corn",
            imageResource = R.drawable.meat_lovers,
            name = "Veggie Delight",
            price = BigDecimal("9.79"),
            numberInCart = 2
        ),
        ProductUi(
            id = "4",
            localId = 4,
            description = "Another food",
            imageResource = R.drawable.strawberry,
            name = "Meat Lovers Pizza",
            price = BigDecimal("13.28"),
            numberInCart = 1
        ),
        ProductUi(
            id = "5",
            localId = 5,
            description = "A delicious food",
            imageResource = R.drawable.mineral_water,
            name = "Hawaiian Pizza",
            price = BigDecimal("8.18")
        ),
        ProductUi(
            id = "6",
            localId = 6,
            description = "Another food",
            imageResource = R.drawable.pepsi,
            name = "Meat Lovers Pizza",
            price = BigDecimal("18.88")
        ),
        ProductUi(
            id = "7",
            localId = 7,
            description = "A delicious food",
            imageResource = R.drawable.spicy_chili_sauce,
            name = "Hawaiian Pizza",
            price = BigDecimal("21.19")
        ),
        ProductUi(
            id = "8",
            localId = 8,
            description = "Another food",
            imageResource = R.drawable.bbq_sauce,
            name = "Meat Lovers Pizza",
            price = BigDecimal("5.43")
        )
    )

val addOnsForPreview =
    listOf(
        ProductUi(
            id = "3",
            localId = 3,
            description = "",
            imageResource = R.drawable.cookies,
            name = "Chocolate Ice Cream",
            price = BigDecimal("10.19")
        ),
        ProductUi(
            id = "4",
            localId = 4,
            description = "",
            imageResource = R.drawable.strawberry,
            name = "Strawberry Ice Cream",
            price = BigDecimal("3.28"),
            numberInCart = 1
        ),
        ProductUi(
            id = "5",
            localId = 5,
            description = "A delicious food",
            imageResource = R.drawable.mineral_water,
            name = "Mineral Water",
            price = BigDecimal("1.18")
        ),
        ProductUi(
            id = "6",
            localId = 6,
            description = "",
            imageResource = R.drawable.pepsi,
            name = "Pepsi",
            price = BigDecimal("18.88")
        ),
        ProductUi(
            id = "7",
            localId = 7,
            description = "",
            imageResource = R.drawable.spicy_chili_sauce,
            name = "Spicy Chili Sauce",
            price = BigDecimal("1.19")
        )
    )

val previewProducts: List<MenuItemUi> =
    listOf(
        MenuItemUi(
            MenuType.Entree,
            productUiListForPreview,
            1
        )
    )

val inCartItemsForPreview =
    listOf(
        InCartItem(
            name = "Chocolate Ice Cream",
            description = "A delicious food",
            imageResource = R.drawable.cookies,
            price = "10.19",
            numberInCart = 1,
            imageUrl = "",
            type = "entree",
            lineNumbers = emptyList(),
            productId = 1,
            toppingsForDisplay =

                mapOf(
                    "Pepperoni" to 2,
                    "Mushrooms" to 2,
                    "Olives" to 1
                ),
            toppings = emptyList()
        ),
        InCartItem(
            name = "Meat Pizza",
            description = "Meat Lovers Pizza",
            toppingsForDisplay =

                mapOf(
                    "Pepperoni" to 2,
                    "Mushrooms" to 2,
                    "Olives" to 1
                ),
            imageResource = R.drawable.meat_lovers,
            price = "20.19",
            numberInCart = 2,
            imageUrl = "",
            type = "entree",
            lineNumbers = emptyList(),
            productId = 1,
            toppings = emptyList()
        ),
        InCartItem(
            name = "Meat Pizza",
            description = "Meat Lovers Pizza",
            toppingsForDisplay =

                mapOf(
                    "Pepperoni" to 2,
                    "Mushrooms" to 2,
                    "Olives" to 1
                ),
            imageResource = R.drawable.meat_lovers,
            price = "20.19",
            numberInCart = 2,
            imageUrl = "",
            type = "entree",
            lineNumbers = emptyList(),
            productId = 1,
            toppings = emptyList()
        )
    )
