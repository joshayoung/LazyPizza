package com.joshayoung.lazypizza.order.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaColors
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.core.ui.theme.surfaceHigher
import com.joshayoung.lazypizza.core.ui.theme.textPrimary
import com.joshayoung.lazypizza.order.domain.models.OrderStatus
import com.joshayoung.lazypizza.order.presentation.models.OrderUi

// TODO: Convert to OrderUi:
@Composable
fun OrderCard(
    orderUi: OrderUi,
    modifier: Modifier = Modifier
) {
    Row(
        modifier =
            modifier
                .height(IntrinsicSize.Max)
                .dropShadow(
                    shape =
                        RoundedCornerShape(20.dp),
                    shadow =
                        Shadow(
                            radius = 16.dp,
                            spread = 6.dp,
                            color =
                                MaterialTheme.colorScheme.textPrimary.copy(alpha = 0.05f),
                            offset = DpOffset(x = 0.dp, 4.dp)
                        )
                ).clip(RoundedCornerShape(10.dp))
                .background(color = MaterialTheme.colorScheme.surfaceHigher)
                .padding(10.dp)
                .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier =
                Modifier
                    .fillMaxHeight()
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(bottom = 10.dp)
            ) {
                Text("Order #${orderUi.number}", style = MaterialTheme.typography.titleMedium)
                Text(orderUi.date)
            }
            Column(
                verticalArrangement = Arrangement.Bottom
            ) {
                orderUi.productsWithCount.forEach { productWithCount ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = productWithCount.number)
                        Text("x")
                        Text(productWithCount.name)
                    }
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier =
                Modifier
                    .fillMaxHeight()
        ) {
            val statusColor =
                when (orderUi.status) {
                    OrderStatus.InProgress -> LazyPizzaColors.inProgress
                    OrderStatus.Completed -> LazyPizzaColors.completed
                    OrderStatus.Unknown -> Color.Transparent
                }
            Text(
                orderUi.status.displayValue,
                modifier =
                    Modifier
                        .clip(
                            RoundedCornerShape(20.dp)
                        ).background(statusColor)
                        .padding(horizontal = 10.dp),
                color = MaterialTheme.colorScheme.surfaceHigher
            )
            Column(horizontalAlignment = Alignment.End) {
                Text("Total amount:")
                Text("$${orderUi.total}", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderCardPreview() {
    LazyPizzaTheme {
        Column(
            modifier =
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OrderCard(
                modifier =
                Modifier,
                orderUi =
                    OrderUi(
                        number = "1234",
                        pickupTime = "Sept 26, 2025",
                        userId = "joe",
                        productsWithCount = emptyList(),
                        total = "12.54",
                        status = OrderStatus.InProgress,
                        date = "bla"
                    )
//                    Order(
//                        number = "123456",
//                        date = "September 25, 12:15",
//                        items =
//                            listOf(
//                                InCartItemUi(
//                                    name = "Meat Pizza",
//                                    description = "Meat Lovers Pizza",
//                                    toppingsForDisplay =
//
//                                        mapOf(
//                                            "Pepperoni" to 2,
//                                            "Mushrooms" to 2,
//                                            "Olives" to 1
//                                        ),
//                                    imageResource = R.drawable.meat_lovers,
//                                    price = "20.19",
//                                    remoteId = "123",
//                                    numberInCart = 2,
//                                    imageUrl = "",
//                                    type = MenuTypeUi.Entree,
//                                    lineNumbers = emptyList(),
//                                    productId = 1,
//                                    toppings = emptyList()
//                                ),
//                                InCartItemUi(
//                                    name = "Pepsi",
//                                    description = "Pepsi",
//                                    toppingsForDisplay =
//
//                                        mapOf(
//                                            "Pepperoni" to 2,
//                                            "Mushrooms" to 2,
//                                            "Olives" to 1
//                                        ),
//                                    imageResource = R.drawable.meat_lovers,
//                                    price = "20.19",
//                                    remoteId = "123",
//                                    numberInCart = 1,
//                                    imageUrl = "",
//                                    type = MenuTypeUi.Entree,
//                                    lineNumbers = emptyList(),
//                                    productId = 1,
//                                    toppings = emptyList()
//                                ),
//                                InCartItemUi(
//                                    name = "Cookies Ice Cream",
//                                    description = "Meat Lovers Pizza",
//                                    toppingsForDisplay =
//
//                                        mapOf(
//                                            "Pepperoni" to 2,
//                                            "Mushrooms" to 2,
//                                            "Olives" to 1
//                                        ),
//                                    imageResource = R.drawable.meat_lovers,
//                                    price = "20.19",
//                                    remoteId = "123",
//                                    numberInCart = 2,
//                                    imageUrl = "",
//                                    type = MenuTypeUi.Entree,
//                                    lineNumbers = emptyList(),
//                                    productId = 1,
//                                    toppings = emptyList()
//                                )
//                            ),
//                        status = OrderStatus.InProgress,
//                        total = "23.21"
//                    )
            )
        }
    }
}
