package com.joshayoung.lazypizza.core.presentation.utils

import androidx.compose.runtime.Composable
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.core.presentation.models.BottomNavItemUi
import com.joshayoung.lazypizza.core.presentation.models.InCartItemUi
import com.joshayoung.lazypizza.core.ui.theme.CartIcon
import com.joshayoung.lazypizza.core.ui.theme.HistoryIcon
import com.joshayoung.lazypizza.core.ui.theme.MenuIcon
import com.joshayoung.lazypizza.history.domain.models.Order
import com.joshayoung.lazypizza.history.domain.models.OrderStatus
import com.joshayoung.lazypizza.menu.presentation.models.MenuItemUi
import com.joshayoung.lazypizza.menu.presentation.models.MenuTypeUi
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi
import java.math.BigDecimal

val previewBottomNavItemUis: List<BottomNavItemUi>
    @Composable
    get() {
        return listOf(
            BottomNavItemUi(
                label = "Menu",
                selected = true,
                clickAction = {
                },
                imageVector = MenuIcon
            ),
            BottomNavItemUi(
                label = "Cart",
                selected = false,
                clickAction = {
                },
                imageVector = CartIcon
            ),
            BottomNavItemUi(
                label = "History",
                selected = false,
                clickAction = {
                },
                imageVector = HistoryIcon
            )
        )
    }

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

val inCartItemsForPreviewUis =
    listOf(
        InCartItemUi(
            name = "Chocolate Ice Cream",
            description = "A delicious food",
            imageResource = R.drawable.cookies,
            price = "10.19",
            numberInCart = 1,
            imageUrl = "",
            type = MenuTypeUi.Entree,
            lineNumbers = emptyList(),
            productId = 1,
            toppingsForDisplay =

                mapOf(
                    "Pepperoni" to 2,
                    "Mushrooms" to 2,
                    "Olives" to 1
                ),
            remoteId = "123",
            toppings = emptyList()
        ),
        InCartItemUi(
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
            remoteId = "123",
            imageUrl = "",
            type = MenuTypeUi.Entree,
            lineNumbers = emptyList(),
            productId = 1,
            toppings = emptyList()
        ),
        InCartItemUi(
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
            remoteId = "123",
            numberInCart = 2,
            imageUrl = "",
            type = MenuTypeUi.Entree,
            lineNumbers = emptyList(),
            productId = 1,
            toppings = emptyList()
        )
    )

val previewProducts: List<MenuItemUi> =
    listOf(
        MenuItemUi(
            MenuTypeUi.Entree,
            inCartItemsForPreviewUis,
            1
        )
    )

val previewOrders =
    listOf(
        Order(
            number = "3456",
            date = "May 15, 9:35",
            items =
                listOf(
                    InCartItemUi(
                        name = "Cheese Pizza",
                        description = "Just Cheese",
                        toppingsForDisplay =
                            mapOf(
                                "Mushrooms" to 2,
                                "Olives" to 1,
                                "Onions" to 4
                            ),
                        imageResource = R.drawable.margherita,
                        price = "14.34",
                        remoteId = "93",
                        numberInCart = 3,
                        imageUrl = "",
                        type = MenuTypeUi.Entree,
                        lineNumbers = emptyList(),
                        productId = 4,
                        toppings = emptyList()
                    )
                ),
            status = OrderStatus.InProgress,
            total = "15.58"
        ),
        Order(
            number = "3456",
            date = "September 5, 2:15",
            items =
                listOf(
                    InCartItemUi(
                        name = "Veggy Pizza",
                        description = "Delicious Vegitables",
                        toppingsForDisplay =
                            mapOf(
                                "Pepperoni" to 2,
                                "Mushrooms" to 2,
                                "Olives" to 1
                            ),
                        imageResource = R.drawable.meat_lovers,
                        price = "10.19",
                        remoteId = "23",
                        numberInCart = 1,
                        imageUrl = "",
                        type = MenuTypeUi.Entree,
                        lineNumbers = emptyList(),
                        productId = 1,
                        toppings = emptyList()
                    )
                ),
            status = OrderStatus.Completed,
            total = "13.21"
        ),
        Order(
            status = OrderStatus.InProgress,
            total = "13.21",
            number = "123456",
            date = "September 25, 12:15",
            items =
                listOf(
                    InCartItemUi(
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
                        remoteId = "123",
                        numberInCart = 2,
                        imageUrl = "",
                        type = MenuTypeUi.Entree,
                        lineNumbers = emptyList(),
                        productId = 1,
                        toppings = emptyList()
                    )
                )
        )
    )