package com.joshayoung.lazypizza.history.data

import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.core.presentation.models.InCartItemUi
import com.joshayoung.lazypizza.history.domain.HistoryRepository
import com.joshayoung.lazypizza.history.domain.models.Order
import com.joshayoung.lazypizza.history.domain.models.OrderStatus
import com.joshayoung.lazypizza.menu.presentation.models.MenuTypeUi

class HistoryRepositoryImpl : HistoryRepository {
    override fun getOrdersFor(user: String): List<Order> {
        return listOf(
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
                status = OrderStatus.InProgress,
                total = "13.21"
            ),
            Order(
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
                    ),
                status = OrderStatus.InProgress,
                total = "23.21"
            )
        )
    }
}