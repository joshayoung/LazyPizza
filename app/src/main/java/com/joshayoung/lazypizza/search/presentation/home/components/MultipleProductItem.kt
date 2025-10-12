package com.joshayoung.lazypizza.search.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.core.presentation.components.LazyImage
import com.joshayoung.lazypizza.search.presentation.models.ProductUi
import com.joshayoung.lazypizza.ui.theme.LazyPizzaTheme

@Composable
fun MultipleProductItem(productUi: ProductUi) {
    val itemCount = remember { mutableIntStateOf(1) }
    Row(
        modifier =
            Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LazyImage(productUi)
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(start = 20.dp)
                    .padding(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ProductHeader(productUi, itemCount)
            Text(productUi.description ?: "")
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (itemCount.intValue == 0) {
                    PriceAndAddButton(productUi.price, itemCount = itemCount)
                } else {
                    PriceAndQuantity(productUi.price, itemCount)
                }
            }
        }
    }
}

@Preview
@Composable
fun MultipleProductItemPreview() {
    LazyPizzaTheme {
        Card(
            modifier =
                Modifier
                    .height(140.dp)
        ) {
            MultipleProductItem(
                productUi =
                    ProductUi(
                        description = "description",
                        imageUrl = "",
                        plImageUrl = "",
                        imageResource = R.drawable.seven_up,
                        name = "7-up",
                        price = "1.23"
                    )
            )
        }
    }
}
