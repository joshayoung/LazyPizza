package com.joshayoung.lazypizza.search.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.search.presentation.models.ProductUi
import com.joshayoung.lazypizza.ui.theme.LazyPizzaTheme
import java.math.BigDecimal

@Composable
fun ProductHeader(
    productUi: ProductUi,
    itemCount: MutableState<Int>
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier =
            Modifier
                .fillMaxWidth()
    ) {
        Text(productUi.name, fontWeight = FontWeight.Bold)
        if (itemCount.value > 0) {
            IconButton(
                onClick = {
                    itemCount.value = 0
                },
                modifier =
                    Modifier
                        .size(20.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.trash),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier =
                    Modifier
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductHeaderPreview() {
    LazyPizzaTheme {
        val itemCount =
            remember {
                mutableIntStateOf(0)
            }
        ProductHeader(
            productUi =
                ProductUi(
                    id = "11",
                    description = "description",
                    imageUrl = "",
                    imageResource = R.drawable.pepsi,
                    name = "Pepsi",
                    price = BigDecimal("1.12")
                ),
            itemCount
        )
    }
}
