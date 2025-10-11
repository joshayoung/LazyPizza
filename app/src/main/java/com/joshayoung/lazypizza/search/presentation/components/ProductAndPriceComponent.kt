package com.joshayoung.lazypizza.search.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.presentation.components.LazyImage
import com.joshayoung.lazypizza.core.presentation.models.ImageResource
import com.joshayoung.lazypizza.ui.theme.LazyPizzaTheme

@Composable
fun ProductAndPriceComponent(
    product: Product,
    modifier: Modifier
) {
    val inPreviewOrDebug = LocalInspectionMode.current || BuildConfig.DEBUG
    Column(
        modifier =
            modifier
                .height(150.dp)
                .border(
                    1.dp,
                    color = MaterialTheme.colorScheme.outline,
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
            LazyImage(
                if (inPreviewOrDebug) {
                    ImageResource.DrawableResource(product.imageResource)
                } else {
                    ImageResource.RemoteFilePath(product.remoteImageUrl)
                },
                modifier =
                    Modifier
                        .size(60.dp)
            )
        }
        Text(
            product.name,
            modifier = Modifier,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Row(
            modifier =
                Modifier
                    .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ClickerButton(R.drawable.minus)
            Text(product.price, style = MaterialTheme.typography.titleMedium)
            ClickerButton(R.drawable.plus)
        }
    }
}

@Composable
fun ClickerButton(imageResource: Int) {
    Button(
        onClick = {},
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(8.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
        modifier =
            Modifier
                .border(1.dp, color = Color.LightGray, shape = RoundedCornerShape(7.dp))
                .height(24.dp)
                .width(24.dp)
    ) {
        Image(
            painter = painterResource(imageResource),
            contentDescription = null,
            modifier =
                Modifier
                    .size(10.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductAndPriceComponentPreview() {
    LazyPizzaTheme {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            ProductAndPriceComponent(
                product =
                    Product(
                        description = "description",
                        imageUrl = "",
                        plImageUrl = "",
                        imageResource = R.drawable.basil,
                        name = "basil",
                        price = "1.1"
                    ),
                modifier = Modifier.size(200.dp)
            )
            ProductAndPriceComponent(
                product =
                    Product(
                        description = "description",
                        imageUrl = "",
                        plImageUrl = "",
                        imageResource = R.drawable.bacon,
                        name = "basil",
                        price = "0.50"
                    ),
                modifier = Modifier.size(200.dp)
            )
        }
    }
}