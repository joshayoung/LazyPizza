package com.joshayoung.lazypizza.menu.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.menu.presentation.details.DetailAction
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi
import com.joshayoung.lazypizza.menu.presentation.models.ToppingUi
import java.math.BigDecimal

@Composable
fun QuantityToggler(
    toppingUi: ToppingUi,
    click: (DetailAction) -> Unit,
    quantity: MutableIntState,
    preventMore: MutableState<Boolean>
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ToggleButton(R.drawable.minus, click = {
            click(DetailAction.DecrementPrice(price = toppingUi.price))
            if (quantity.intValue > 0) {
                quantity.intValue -= 1
            }
            click(DetailAction.RemoveTopping(toppingUi))
        })
        Text(
            quantity.intValue.toString(),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
        )
        ToggleButton(
            R.drawable.plus,
            click = {
                if (!preventMore.value) {
                    click(DetailAction.IncrementPrice(price = toppingUi.price))
                    quantity.intValue += 1
                    click(DetailAction.AddTopping(toppingUi))
                }
            },
            preventMore
        )
    }
}

@Composable
@Preview(showBackground = true)
fun QuantityClickerPreview() {
    val quantity = remember { mutableIntStateOf(0) }
    val preventMore = remember { mutableStateOf(false) }
    LazyPizzaTheme {
        QuantityToggler(
            ToppingUi(
                localId = 3,
                imageUrl = "",
                imageResource = R.drawable.bacon,
                name = "basil",
                price = BigDecimal("0.50"),
                remoteId = ""
            ),
            click = {},
            quantity = quantity,
            preventMore = preventMore
        )
    }
}