package com.joshayoung.lazypizza.cart.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.room.util.TableInfo
import com.google.devtools.ksp.symbol.impl.modifierMap
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.core.presentation.components.ProductOrToppingImage
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaColors
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.core.ui.theme.surfaceHigher
import com.joshayoung.lazypizza.core.ui.theme.textPrimary
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi
import java.math.BigDecimal

@Composable
fun AddOn(
    productUi: ProductUi,
    modifier: Modifier = Modifier
) {
    Box(
        modifier =
            modifier
                .border(
                    1.dp,
                    color = MaterialTheme.colorScheme.surfaceHigher,
                    shape =

                        RoundedCornerShape(10.dp)
                )
                .dropShadow(
                    shape =
                        RoundedCornerShape(10.dp),
                    shadow =
                        Shadow(
                            radius = 2.dp,
                            spread = 1.dp,
                            color =
                                MaterialTheme.colorScheme.textPrimary.copy(
                                    alpha = 0.1f
                                ),
                            offset = DpOffset(x = 0.dp, 0.dp)
                        )
                )
                .clip(RoundedCornerShape(10.dp))
    ) {
        Column(
            modifier =
                Modifier
                    .background(LazyPizzaColors.surfaceHighest),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProductOrToppingImage(
                imageResource = productUi.imageResource,
                remoteImage = productUi.remoteImageUrl,
                modifier = Modifier
                    .size(100.dp)
            )
            Column(
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.surfaceHigher)
                        .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    productUi.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "$0.59",
                        style = MaterialTheme.typography.titleLarge
                    )
//                Button(
//                    onClick = {},
//                    contentPadding = PaddingValues(0.dp),
//                    modifier =
//                        Modifier
//                            .padding(0.dp)
//                ) {
//                    Text("click", modifier = Modifier.padding(0.dp))
//                }
                    Box(
                        modifier =
                            Modifier
                                .clickable {
                                }.border(
                                    1.dp,
                                    color = MaterialTheme.colorScheme.outlineVariant,
                                    shape = RoundedCornerShape(8.dp)
                                ).padding(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.plus),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier =
                            Modifier
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AddOnPreview() {
    LazyPizzaTheme {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AddOn(
                modifier =
                    Modifier
                        .width(150.dp),
                productUi =
                    ProductUi(
                        id = "2",
                        localId = 2,
                        imageResource = R.drawable.garlic_sauce,
                        name = "Garlic Sauce",
                        price = BigDecimal("0.59"),
                        numberInCart = 2
                    )
            )
        }
    }
}
