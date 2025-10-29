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
import androidx.compose.foundation.lazy.grid.items
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
import com.joshayoung.lazypizza.core.data.database.entity.ToppingInCartEntity
import com.joshayoung.lazypizza.core.presentation.components.ProductOrToppingImage
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.core.ui.theme.surfaceHigher
import com.joshayoung.lazypizza.menu.presentation.home.components.AddButtonWithPrice
import com.joshayoung.lazypizza.menu.presentation.home.components.PriceAndQuantityToggle
import com.joshayoung.lazypizza.menu.presentation.home.components.ProductHeader
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi
import java.math.BigDecimal

@Composable
fun CartItem(
    productUi: ProductUi,
    modifier: Modifier = Modifier,
    onAction: (CartAction) -> Unit
) {
    var itemCount by remember { mutableIntStateOf(productUi.numberInCart) }
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
            productUi.imageResource,
            productUi.imageUrl,
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
            ProductHeader(itemCount, productUi, onAction = {
                onAction(CartAction.RemoveAllFromCart(productUi))
            }) {
                if (itemCount > 0) {
                    itemCount = 0
                }
            }

            productUi.toppings?.let { toppings ->
                LazyColumn {
                    items(toppings) { topping ->
                        Row( horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(topping.numberOfToppings.toString(), color = Color.Gray)
                            Text("x", color = Color.Gray)
                            Text(topping.name, color = Color.Gray)
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
                if (itemCount <= 0) {
                    AddButtonWithPrice(
                        productUi,
                        onAction =
                            {
                                onAction(CartAction.AddItemToCart(productUi))
                            }
                    ) {
                        itemCount += it
                    }
                } else {
                    PriceAndQuantityToggle(
                        productUi,
                        itemCount,
                        increment = {
                            onAction(CartAction.AddItemToCart(productUi))
                        },
                        decrement = {
                            onAction(CartAction.RemoveItemFromCart(productUi))
                        }
                    ) {
                        if (itemCount > 0) {
                            itemCount += it
                        }
                    }
                }
            }
        }
    }
}

@Preview()
@Composable
fun SideItemPreview() {
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
                        .height(240.dp)
            ) {
                CartItem(
                    productUi =
                        ProductUi(
                            id = "10",
                            localId = 1,
                            description = "description",
                            numberInCart = 10,
                            imageUrl = "",
                            imageResource = R.drawable.meat_lovers,
                            name = "Meat Lovers Pizza",
                            price = BigDecimal("12.23"),
                            toppings =
                                listOf(
                                    ToppingInCartEntity(
                                        name = "Chili Peppers",
                                        price = "0.43",
                                        remoteId = 1,
                                        toppingId = 1,
                                        imageUrl = "",
                                        productId = 2,
                                        numberOfToppings = 2
                                    ),
                                    ToppingInCartEntity(
                                        name = "Extra Cheese",
                                        price = "0.65",
                                        remoteId = 1,
                                        imageUrl = "",
                                        toppingId = 1,
                                        productId = 2,
                                        numberOfToppings = 3
                                    ),
                                    ToppingInCartEntity(
                                        name = "Olives",
                                        toppingId = 1,
                                        price = "0.25",
                                        remoteId = 1,
                                        imageUrl = "",
                                        productId = 4,
                                        numberOfToppings = 1
                                    )
                                )
                        ),
                    onAction = {}
                )
            }
        }
    }
}
