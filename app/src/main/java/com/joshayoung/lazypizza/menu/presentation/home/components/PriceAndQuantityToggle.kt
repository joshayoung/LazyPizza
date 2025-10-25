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
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.menu.presentation.home.HomeAction
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi
import java.math.BigDecimal
import java.util.Locale

@Composable
fun PriceAndQuantityToggle(
    productUi: ProductUi,
    itemCount: Int,
    onAction: (HomeAction) -> Unit,
    updateCart: (Int) -> Unit
) {
    val calculatedPrice = itemCount * productUi.price.toDouble()
    Row(
        modifier =
            Modifier
                .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        QuantitySelector(
            itemCount,
            onAction = onAction,
            productUi = productUi,
            updateCart = updateCart
        )
        PriceWithNumber(itemCount, calculatedPrice)
    }
}

@Composable
fun PriceWithNumber(
    itemCount: Int,
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
            Text(itemCount.toString(), fontSize = 10.sp)
            Text(
                "x",
                fontSize = 10.sp,
                modifier =
                    Modifier
                        .padding(horizontal = 4.dp)
            )
            Text(price, fontSize = 10.sp)
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
                ProductUi(
                    id = "",
                    localId = 2,
                    description = "description",
                    imageUrl = "",
                    imageResource = R.drawable.cart,
                    name = "name",
                    price = BigDecimal("1.22")
                ),
                1,
                onAction = {},
                updateCart = {}
            )
        }
    }
}
