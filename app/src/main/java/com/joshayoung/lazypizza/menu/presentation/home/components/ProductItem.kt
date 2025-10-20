package com.joshayoung.lazypizza.menu.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.core.presentation.components.PizzaImage
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi
import com.joshayoung.lazypizza.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.ui.theme.surfaceHigher
import java.math.BigDecimal
import java.util.Locale
import kotlin.String
import kotlin.Unit

@Composable
fun ProductItem(
    productUi: ProductUi,
    goToDetails: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier =
            modifier
                .border(
                    1.dp,
                    color = MaterialTheme.colorScheme.surfaceHigher,
                    shape = RoundedCornerShape(12.dp)
                ).dropShadow(
                    shape = RoundedCornerShape(20.dp),
                    shadow =
                        Shadow(
                            radius = 4.dp,
                            spread = 2.dp,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            offset = DpOffset(x = 2.dp, 2.dp)
                        )
                ).clickable {
                    goToDetails(productUi.id)
                },
        verticalAlignment = Alignment.CenterVertically
    ) {
        PizzaImage(
            productUi,
            modifier =
                Modifier
                    .fillMaxHeight()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                    )
        )
        Column(
            modifier =
                Modifier
                    .background(
                        MaterialTheme.colorScheme.surface,
                        shape =
                            RoundedCornerShape(
                                topEnd = 12.dp,
                                bottomEnd = 12.dp
                            )
                    ).padding(start = 20.dp)
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                productUi.name,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
            )
            Text(
                productUi.description ?: "",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.weight(1f))
            val price = String.format(locale = Locale.US, "$%.2f", productUi.price)
            Text(
                price,
                modifier = Modifier,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Preview
@Composable
private fun ProductItemPreview() {
    LazyPizzaTheme {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(20.dp)
        ) {
            Column(
                modifier =
                    Modifier
                        .height(200.dp)
                        .width(400.dp)
            ) {
                ProductItem(
                    productUi =
                        ProductUi(
                            id = "1",
                            description = "onion, corn",
                            imageResource = R.drawable.veggie_delight,
                            name = "Veggie Delight",
                            price = BigDecimal("9.79")
                        ),
                    goToDetails = {}
                )
            }

            Column(
                modifier =
                    Modifier
                        .height(100.dp)
                        .width(400.dp)
            ) {
                ProductItem(
                    productUi =
                        ProductUi(
                            id = "1",
                            description =
                                "Tomato sauce, mozzarella, mushrooms, " +
                                    "olives, bell pepper, onion, corn",
                            imageResource = R.drawable.veggie_delight,
                            name = "Veggie Delight",
                            price = BigDecimal("9.79")
                        ),
                    goToDetails = {}
                )
            }
        }
    }
}
