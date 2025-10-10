package com.joshayoung.lazypizza.search.presentation.details

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.core.presentation.components.LazyPizzaAppBar
import com.joshayoung.lazypizza.core.presentation.components.LazyPizzaScaffold
import com.joshayoung.lazypizza.search.data.models.Product
import com.joshayoung.lazypizza.search.presentation.components.ProductAndPriceComponent
import com.joshayoung.lazypizza.ui.theme.LazyPizzaTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreenRoot(
    viewModel: DetailsScreenViewModel = koinViewModel(),
    navigateBack: () -> Unit
) {
    DetailsScreen(state = viewModel.state, navigateBack = navigateBack)
}

@Composable
fun DetailsScreen(
    state: DetailsState,
    navigateBack: () -> Unit
) {
    LazyPizzaScaffold(
        topAppBar = {
            LazyPizzaAppBar(
                showLogo = false,
                showContact = false,
                showBackButton = true,
                onBackClick = navigateBack
            )
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
                        .padding(bottom = 20.dp)
                        .background(
                            Color.LightGray.copy(alpha = 0.2f)
                        ),
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
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(0.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                ) {
                    stickyHeader {
                        Text(
                            "Add Extra Toppings".uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    items(state.toppings) { topping ->
                        ProductAndPriceComponent(
                            topping,
                            token = state.token,
                            modifier = Modifier
                        )
                    }
                }

                Box(
                    modifier =
                        Modifier
                            .shadow(
                                2.dp,
                                RoundedCornerShape(16.dp),
                                ambientColor = MaterialTheme.colorScheme.primary
                            )
                ) {
                    Button(onClick = {}, shape = RoundedCornerShape(16.dp)) {
                        Text(
                            "Add to Cart for $12.99",
                            modifier =
                                Modifier
                                    .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun DetailsScreenPreview() {
    LazyPizzaTheme {
        DetailsScreen(
            navigateBack = {},
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
                                name = "bacon",
                                price = "1.00",
                                imageResource = R.drawable.bacon
                            ),
                            Product(
                                name = "extra cheese",
                                price = "1.10",
                                imageResource = R.drawable.cheese
                            ),
                            Product(
                                name = "corn",
                                price = ".10",
                                imageResource = R.drawable.corn
                            ),
                            Product(
                                name = "tomato",
                                price = ".10",
                                imageResource = R.drawable.tomato
                            ),
                            Product(
                                name = "olives",
                                price = ".10",
                                imageResource = R.drawable.olive
                            ),
                            Product(
                                name = "pepperoni",
                                price = ".10",
                                imageResource = R.drawable.pepperoni
                            )
                        )
                )
        )
    }
}