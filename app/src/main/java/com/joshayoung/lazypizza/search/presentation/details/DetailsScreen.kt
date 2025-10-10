package com.joshayoung.lazypizza.search.presentation.details

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.core.presentation.components.LazyPizzaAppBar
import com.joshayoung.lazypizza.core.presentation.components.LazyPizzaScaffold
import com.joshayoung.lazypizza.search.ImageResource
import com.joshayoung.lazypizza.search.data.models.Product
import com.joshayoung.lazypizza.search.presentation.components.LazyImage
import com.joshayoung.lazypizza.ui.theme.LazyPizzaTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreenRoot(viewModel: DetailsScreenViewModel = koinViewModel()) {
    DetailsScreen(state = viewModel.state)
}

@Composable
fun DetailsScreen(state: DetailsState) {
    LazyPizzaScaffold(
        topAppBar = {
            LazyPizzaAppBar()
        }
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.meat_lovers),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier =
                        Modifier
                            .size(300.dp)
                )
            }
            Column(
                modifier =
                    Modifier
                        .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = state.product?.name ?: "",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                )
                Text(
                    text = state.product?.description ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier =
                        Modifier
                            .padding(bottom = 10.dp)
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3)
                ) {
                    stickyHeader {
                        Text(
                            "Add Extra Toppings".uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                    items(state.toppings) { topping ->
                        ExtraTopping(
                            topping,
                            token = state.token,
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExtraTopping(
    product: Product,
    token: String?,
    modifier: Modifier
) {
    Column(
        modifier =
            modifier
                .padding(10.dp)
                .height(150.dp)
                .border(
                    1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(15.dp)
                ).padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier =
                Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    .padding(4.dp)
        ) {
            LazyImage(
                // TODO: Move this to a debug/preview check:
                if (product.imageResource != null) {
                    ImageResource.DrawableResource(product.imageResource)
                } else {
                    ImageResource.RemoteFilePath(product.remoteImageUrl, token = token)
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
        Text(product.price, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
@Preview
fun DetailsScreenPreview() {
    LazyPizzaTheme {
        DetailsScreen(
            state =
                DetailsState(
                    product =
                        Product(
                            name = "Margherita",
                            description = "Tomato sauce, Mozzarella, Fresh basic, Olive oil",
                            price = "1.00"
                        ),
                    toppings =
                        listOf(
                            Product(
                                name = "BBQ Sauce",
                                price = "1.00",
                                imageResource = R.drawable.bbq_sauce
                            ),
                            Product(
                                name = "Spicy Chili Sauce",
                                price = "1.10",
                                imageResource = R.drawable.spicy_chili_sauce
                            ),
                            Product(
                                name = "Basil",
                                price = ".10",
                                imageResource = R.drawable.basil
                            ),
                            Product(
                                name = "Pinapple",
                                price = ".10",
                                imageResource = R.drawable.pineapple
                            ),
                            Product(
                                name = "Olive",
                                price = ".10",
                                imageResource = R.drawable.olive
                            )
                        )
                )
        )
    }
}