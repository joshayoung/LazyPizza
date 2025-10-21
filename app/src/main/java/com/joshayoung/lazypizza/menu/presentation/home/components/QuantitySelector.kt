package com.joshayoung.lazypizza.menu.presentation.home.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.menu.presentation.home.HomeAction
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi
import java.math.BigDecimal

@Composable
fun QuantitySelector(
    itemCount: Int,
    productUi: ProductUi,
    onAction: (HomeAction) -> Unit,
    updateCart: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier =
        Modifier
    ) {
        CountButton(
            "-",
            itemCount,
            onAction = { onAction(HomeAction.RemoveItemFromCart(productUi)) },
            {
                if (itemCount > 0) {
                    updateCart(-1)
                }
            }
        )
        Text(
            text = itemCount.toString(),
            style = MaterialTheme.typography.titleMedium,
            modifier =
                Modifier
                    .padding(horizontal = 20.dp)
        )
        CountButton("+", itemCount, onAction = { onAction(HomeAction.AddItemToCart(productUi)) }, {
            updateCart(1)
        })
    }
}

@Composable
fun CountButton(
    text: String,
    itemCount: Int,
    onAction: () -> Unit,
    action: () -> Unit
) {
    Button(
        contentPadding = PaddingValues(0.dp),
        modifier =
            Modifier
                .border(1.dp, Color.LightGray, shape = RoundedCornerShape(4.dp))
                .size(20.dp),
        onClick = {
            action()
            onAction()
        },
        colors =
            ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
    ) {
        Text(
            color = MaterialTheme.colorScheme.onSurface,
            text = text,
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuantitySelectorPreview() {
    LazyPizzaTheme {
        QuantitySelector(
            itemCount = 1,
            ProductUi(
                id = "1",
                description = "",
                imageUrl = "",
                imageResource = R.drawable.cart,
                name = "",
                price = BigDecimal("12")
            ),
            onAction = {},
            updateCart = {}
        )
    }
}
