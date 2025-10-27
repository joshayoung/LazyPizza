package com.joshayoung.lazypizza.menu.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.core.presentation.components.PizzaImage
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.menu.presentation.details.DetailAction
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi
import com.joshayoung.lazypizza.menu.presentation.models.ToppingUi
import java.math.BigDecimal
import java.util.Locale

@Composable
fun Topping(
    toppingUi: ToppingUi,
    modifier: Modifier,
    click: (DetailAction) -> Unit
) {
    var incrementMode by remember { mutableStateOf(false) }
    val quantity = remember { mutableIntStateOf(1) }
    val preventMore = remember { mutableStateOf(false) }

    if (quantity.intValue < 1) {
        incrementMode = false
        quantity.intValue = 1
        preventMore.value = false
    }

    if (quantity.intValue > 2) {
        preventMore.value = true
    }

    val borderColor =
        if (incrementMode) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outline
        }

    Column(
        modifier =
            modifier
                .height(150.dp)
                .clickable {
                    incrementMode = true
                    click(DetailAction.IncrementPrice(toppingUi.price))
                    click(DetailAction.AddToppingToList(toppingUi))
                }.border(
                    1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(15.dp)
                ).padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier =
                Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    .padding(4.dp)
        ) {
            PizzaImage(
                toppingUi.imageResource,
                toppingUi.imageUrl,
                modifier =
                    Modifier
                        .size(60.dp)
            )
        }
        Text(
            toppingUi.name,
            modifier = Modifier,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )
        if (incrementMode) {
            QuantityToggler(toppingUi, click, quantity, preventMore)
        } else {
            val price = String.format(Locale.US, "$%.2f", toppingUi.price)
            Text(
                price,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ToppingPreview() {
    LazyPizzaTheme {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Topping(
                toppingUi =
                    ToppingUi(
                        localId = 2,
                        imageUrl = "",
                        imageResource = R.drawable.basil,
                        name = "basil",
                        price = BigDecimal("1.10"),
                        remoteId = ""
                    ),
                modifier = Modifier.size(200.dp),
                click = {}
            )
            Topping(
                toppingUi =
                    ToppingUi(
                        localId = 3,
                        imageUrl = "",
                        imageResource = R.drawable.bacon,
                        name = "basil",
                        price = BigDecimal("0.50"),
                        remoteId = ""
                    ),
                modifier = Modifier.size(200.dp),
                click = {}
            )
        }
    }
}