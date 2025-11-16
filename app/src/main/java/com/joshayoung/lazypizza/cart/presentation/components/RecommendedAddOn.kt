package com.joshayoung.lazypizza.cart.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi

@Composable
fun RecommendedAddOns(
    addOns: List<ProductUi>,
    addProductToCart: (productUi: ProductUi) -> Unit
) {
    Column {
        Text(
            "Recommended to Add to Your Order".uppercase(),
            fontSize = 14.sp,
            modifier =
                Modifier
                    .padding(bottom = 10.dp)
        )
        LazyRow(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(addOns) { productUi ->
                AddOn(
                    productUi,
                    addToCart =
                        {
                            addProductToCart(productUi)
                        },
                    modifier =
                        Modifier
                            .width(140.dp)
                            .height(220.dp)
                )
            }
        }
    }
}
