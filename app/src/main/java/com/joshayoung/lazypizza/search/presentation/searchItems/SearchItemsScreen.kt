package com.joshayoung.lazypizza.search.presentation.searchItems

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.core.presentation.components.LazyPizzaAppBar
import com.joshayoung.lazypizza.core.presentation.components.LazyPizzaScaffold
import com.joshayoung.lazypizza.search.ImageResource
import com.joshayoung.lazypizza.search.data.models.AllProducts
import com.joshayoung.lazypizza.search.data.models.Product
import com.joshayoung.lazypizza.search.presentation.components.LazyImage
import com.joshayoung.lazypizza.search.presentation.components.SearchField
import com.joshayoung.lazypizza.ui.theme.LazyPizzaTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchItemsScreenRoot(
    viewModel: SearchItemsViewModel = koinViewModel(),
    goToDetails: (id: Int) -> Unit
) {
    SearchItemsScreen(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        goToDetails = goToDetails
    )
}

@Composable
fun SearchItemsScreen(
    state: SearchItemsState,
    goToDetails: (id: Int) -> Unit
) {
    LazyPizzaScaffold(
        topAppBar = { LazyPizzaAppBar() }
    ) { innerPadding ->

        val listState = remember { LazyListState() }
        val coroutineScope = rememberCoroutineScope()

        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .background(Color(0xFFFAFBFC))
                    .padding(horizontal = 20.dp)
        ) {
            Image(
                painterResource(id = R.drawable.pizza_header),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier =
                    Modifier
                        .fillMaxWidth()
            )
            SearchField(state.search)
            val options = listOf("Pizza", "Drinks", "Sauces", "Ice Cream")
            val selectedIndex by remember { mutableIntStateOf(-1) }
            Row {
                options.forEachIndexed { index, label ->
                    val selected = index == selectedIndex
                    AssistChip(
                        modifier =
                            Modifier
                                .padding(end = 4.dp),
                        onClick = {
                            coroutineScope.launch {
                                if (index == 0) {
                                    listState.animateScrollToItem(state.pizzaScrollPosition)
                                }
                                if (index == 1) {
                                    listState.animateScrollToItem(state.drinkScrollPosition)
                                }
                                if (index == 2) {
                                    listState.animateScrollToItem(state.sauceScrollPosition)
                                }
                                if (index == 3) {
                                    listState.animateScrollToItem(state.iceCreamScrollPosition)
                                }
                            }
                        },
                        label = { Text(label) },
                        leadingIcon = null,
                        shape = RoundedCornerShape(8.dp),
                        colors =
                            AssistChipDefaults.assistChipColors(
                                containerColor =
                                    if (selected) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.surface
                                    },
                                labelColor =
                                    if (selected) {
                                        MaterialTheme.colorScheme.onPrimary
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    }
                            )
                    )
                }
            }
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.items.forEach { iii ->
                        stickyHeader {
                            Text(iii.name)
                        }
                        items(iii.items) { product ->
                            ItemAndPrice(product, state.token, goToDetails = goToDetails)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemAndPrice(
    product: Product,
    token: String?,
    goToDetails: (id: Int) -> Unit
) {
    Card(
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 1.dp
            ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surface),
        modifier =
            Modifier
                .height(140.dp)
                .fillMaxWidth()
                .clickable {
                    goToDetails(11)
                }
    ) {
        Row(
            modifier =
                Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LazyImage(
                // TODO: Move this to a debug/preview check:
                if (product.imageResource != null) {
                    ImageResource.DrawableResource(product.imageResource)
                } else {
                    ImageResource.RemoteFilePath(product.remoteImageUrl, token = token)
                }
            )
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(start = 20.dp)
                        .padding(vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(product.name, fontWeight = FontWeight.Bold)
                Text(product.description ?: "")
                Text(product.price, style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchItemsScreenPreview() {
    LazyPizzaTheme {
        SearchItemsScreen(
            state =
                SearchItemsState(
                    items =
                        listOf(
                            AllProducts(
                                name = "Pizzas",
                                items =
                                    listOf(
                                        Product(
                                            description = "A delicious food",
                                            imageResource = R.drawable.hawaiian,
                                            name = "Hawaiian Pizza",
                                            price = "$10.19"
                                        ),
                                        Product(
                                            description = "Another food",
                                            imageResource = R.drawable.meat_lovers,
                                            name = "Meat Lovers Pizza",
                                            price = "$12.98"
                                        )
                                    )
                            ),
                            AllProducts(
                                name = "Ice Cream",
                                items =
                                    listOf(
                                        Product(
                                            description = "A delicious food",
                                            imageResource = R.drawable.cookies,
                                            name = "Hawaiian Pizza",
                                            price = "$10.19"
                                        ),
                                        Product(
                                            description = "Another food",
                                            imageResource = R.drawable.strawberry,
                                            name = "Meat Lovers Pizza",
                                            price = "$12.98"
                                        )
                                    )
                            ),
                            AllProducts(
                                name = "Drinks",
                                items =
                                    listOf(
                                        Product(
                                            description = "A delicious food",
                                            imageResource = R.drawable.mineral_water,
                                            name = "Hawaiian Pizza",
                                            price = "$10.19"
                                        ),
                                        Product(
                                            description = "Another food",
                                            imageResource = R.drawable.pepsi,
                                            name = "Meat Lovers Pizza",
                                            price = "$12.98"
                                        )
                                    )
                            ),
                            AllProducts(
                                name = "Sauces",
                                items =
                                    listOf(
                                        Product(
                                            description = "A delicious food",
                                            imageResource = R.drawable.spicy_chili_sauce,
                                            name = "Hawaiian Pizza",
                                            price = "$10.19"
                                        ),
                                        Product(
                                            description = "Another food",
                                            imageResource = R.drawable.bbq_sauce,
                                            name = "Meat Lovers Pizza",
                                            price = "$12.98"
                                        )
                                    )
                            )
                        )
                ),
            goToDetails = {}
        )
    }
}
