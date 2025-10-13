package com.joshayoung.lazypizza.search.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.core.presentation.components.LazyImage
import com.joshayoung.lazypizza.search.presentation.models.ProductUi
import com.joshayoung.lazypizza.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.ui.theme.surfaceHighest
import java.math.BigDecimal

@Composable
fun MultipleProductItem(productUi: ProductUi) {
    val itemCount = remember { mutableIntStateOf(0) }
    Row(
        modifier =
            Modifier
                .background(
                    MaterialTheme.colorScheme.surfaceHighest,
                    shape = RoundedCornerShape(12.dp)
                ).border(1.dp, color = Color.White, shape = RoundedCornerShape(12.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LazyImage(productUi)
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            ProductHeader(productUi, itemCount)
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
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
        Column(
            modifier =
                Modifier
                    .height(200.dp)
                    .width(400.dp)
        ) {
            MultipleProductItem(
                productUi =
                    ProductUi(
                        id = "10",
                        description = "description",
                        imageUrl = "",
                        imageResource = R.drawable.seven_up,
                        name = "7-up",
                        price = BigDecimal("1.23")
                    )
            )
        }
    }
}
