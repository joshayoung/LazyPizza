package com.joshayoung.lazypizza.menu.presentation.home.components

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
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.menu.presentation.home.HomeAction
import java.math.BigDecimal
import java.util.Locale

@Composable
fun PriceAndQuantityToggle(
    price: BigDecimal,
    itemCount: Int,
    onAction: (HomeAction) -> Unit,
    updateCart: (Int) -> Unit
) {
    val calculatedPrice = itemCount * price.toDouble()
    Row(
        modifier =
            Modifier
                .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        QuantitySelector(itemCount, onAction = onAction, updateCart = updateCart)
        PriceWithNumber(itemCount, price, calculatedPrice, updateCart = updateCart)
    }
}

@Composable
fun PriceWithNumber(
    itemCount: Int,
    price: BigDecimal,
    calculatedPrice: Double,
    updateCart: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.End,
        modifier =
        Modifier
    ) {
        val price = String.format(locale = Locale.US, "$%.2f", calculatedPrice)
        Text(
            price,
            modifier = Modifier
        )
        Row(modifier = Modifier, verticalAlignment = Alignment.Top) {
            Text(itemCount.toString(), fontSize = 10.sp)
            Text(
                "x",
                fontSize = 10.sp,
                modifier =
                    Modifier
                        .padding(horizontal = 4.dp)
            )
            Text(price.toString(), fontSize = 10.sp)
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
            PriceAndQuantityToggle(
                BigDecimal("1.20"),
                1,
                onAction = {},
                updateCart = {}
            )
        }
    }
}
