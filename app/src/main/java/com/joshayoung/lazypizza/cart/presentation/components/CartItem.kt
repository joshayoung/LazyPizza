package com.joshayoung.lazypizza.cart.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.cart.presentation.CartAction
import com.joshayoung.lazypizza.core.domain.models.InCartItem
import com.joshayoung.lazypizza.core.presentation.components.ProductOrToppingImage
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.core.ui.theme.surfaceHigher
import com.joshayoung.lazypizza.menu.presentation.home.components.AddButtonWithPrice
import com.joshayoung.lazypizza.menu.presentation.home.components.PriceAndQuantityToggle
import com.joshayoung.lazypizza.menu.presentation.home.components.ProductHeader
import java.math.BigDecimal

@Composable
fun CartItem(
    inCartItem: InCartItem,
    modifier: Modifier = Modifier,
    onAction: (CartAction) -> Unit
) {
    Row(
        modifier =
            modifier
                .border(
                    1.dp,
                    color = MaterialTheme.colorScheme.surfaceHigher,
                    shape = RoundedCornerShape(12.dp)
                ).dropShadow(
                    shape = RoundedCornerShape(20.dp),
                    shadow =
                        Shadow(
                            radius = 4.dp,
                            spread = 2.dp,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            offset = DpOffset(x = 2.dp, 2.dp)
                        )
                ).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProductOrToppingImage(
            inCartItem.imageResource,
            inCartItem.imageUrl,
            modifier =
                Modifier
                    .fillMaxHeight()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                    )
        )
        Column(
            modifier =
                Modifier
                    .fillMaxHeight()
                    .background(
                        MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                    ).padding(10.dp),
            verticalArrangement = Arrangement.Top
        ) {
            ProductHeader(inCartItem.numberInCart, inCartItem.name, onAction = {
                onAction(CartAction.RemoveAllFromCart(inCartItem))
            })

            inCartItem.toppingsForDisplay.let { toppings ->
                LazyColumn {
                    items(toppings.entries.toList()) { topping ->
                        val (name, count) = topping
                        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(name, color = Color.Gray)
                            Text("x", color = Color.Gray)
                            Text(count.toString(), color = Color.Gray)
                        }
                    }
                }
            }
            Spacer(Modifier.weight(1f))

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (inCartItem.numberInCart <= 0) {
                    AddButtonWithPrice(
                        BigDecimal(inCartItem.price),
                        onAction =
                            {
                                onAction(CartAction.AddItemToCart(inCartItem))
                            }
                    )
                } else {
                    PriceAndQuantityToggle(
                        totalPrice = BigDecimal(inCartItem.price),
                        inCart = true,
                        price = BigDecimal(inCartItem.price),
                        inCartItem.numberInCart,
                        increment = {
                            onAction(CartAction.AddItemToCart(inCartItem))
                        },
                        decrement = {
                            onAction(CartAction.RemoveItemFromCart(inCartItem))
                        }
                    )
                }
            }
        }
    }
}

@Preview()
@Composable
fun CartItemPreview() {
    LazyPizzaTheme {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier =
                    Modifier
                        .height(140.dp)
            ) {
                CartItem(
                    inCartItem =
                        InCartItem(
                            name = "Meat Pizza",
                            description = "Meat Lovers Pizza",
                            imageResource = R.drawable.meat_lovers,
                            price = "20.19",
                            numberInCart = 2,
                            imageUrl = "",
                            productId = 1,
                            toppingsForDisplay =
                                mapOf(
                                    "Pepperoni" to 2,
                                    "Mushrooms" to 2,
                                    "Olives" to 1
                                ),
                            type = "entree",
                            lineNumbers = emptyList(),
                            remoteId = "",
                            toppings = emptyList()
                        ),
                    onAction = {}
                )
            }
        }
    }
}
