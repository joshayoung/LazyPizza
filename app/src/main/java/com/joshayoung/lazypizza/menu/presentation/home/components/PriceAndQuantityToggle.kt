package com.joshayoung.lazypizza.menu.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshayoung.lazypizza.menu.presentation.home.HomeAction
import com.joshayoung.lazypizza.ui.theme.LazyPizzaTheme
import java.math.BigDecimal
import java.util.Locale

@Composable
fun PriceAndQuantityToggle(
    price: BigDecimal,
    itemCount: MutableState<Int>,
    onAction: (HomeAction) -> Unit
) {
    val calculatedPrice = itemCount.value * price.toDouble()
    Row(
        modifier =
            Modifier
                .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        QuantitySelector(itemCount, onAction = onAction)
        PriceWithNumber(itemCount, price, calculatedPrice)
    }
}

@Composable
fun PriceWithNumber(
    itemCount: MutableState<Int>,
    price: BigDecimal,
    calculatedPrice: Double
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
            Text(itemCount.value.toString(), fontSize = 10.sp)
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
        val itemCount = remember { mutableIntStateOf(1) }
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
        ) {
            PriceAndQuantityToggle(
                BigDecimal("1.20"),
                itemCount,
                onAction = {}
            )
        }
    }
}
