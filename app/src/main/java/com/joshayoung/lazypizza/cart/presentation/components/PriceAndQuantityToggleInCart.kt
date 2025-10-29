package com.joshayoung.lazypizza.cart.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshayoung.lazypizza.core.presentation.components.QuantitySelector
import com.joshayoung.lazypizza.core.presentation.components.QuantitySelectorTwo
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import java.math.BigDecimal
import java.util.Locale

@Composable
fun PriceAndQuantityToggleInCart(
    totalPrice: BigDecimal,
    inCart: Boolean,
    price: BigDecimal,
    itemCount: Int,
    increment: () -> Unit,
    decrement: () -> Unit
) {
    val calculatedPrice = totalPrice
    Row(
        modifier =
            Modifier
                .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        QuantitySelectorTwo(
            itemCount,
            increment = increment,
            decrement = decrement,
            inCart = inCart
        )
        PriceWithNumber(itemCount, price, calculatedPrice.toDouble())
    }
}

@Composable
fun PriceWithNumber(
    itemCount: Int,
    price: BigDecimal,
    calculatedPrice: Double
) {
    Column(
        horizontalAlignment = Alignment.End,
        modifier =
        Modifier
    ) {
        val total = String.format(locale = Locale.US, "$%.2f", calculatedPrice)
        Text(
            total,
            modifier = Modifier
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.End
        ) {
            Text(itemCount.toString(), fontSize = 10.sp)
            Text(
                "x",
                fontSize = 10.sp,
                modifier =
                    Modifier
                        .padding(horizontal = 4.dp)
            )
            Text("$$price", fontSize = 10.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PriceAndQuantityTogglePreview() {
    LazyPizzaTheme {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
        ) {
            PriceAndQuantityToggleInCart(
                totalPrice = BigDecimal(2.33),
                inCart = true,
                price = BigDecimal(23.33),
                1,
                increment = {},
                decrement = {}
            )
        }
    }
}
