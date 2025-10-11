package com.joshayoung.lazypizza.search.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.core.presentation.components.LazyImage
import com.joshayoung.lazypizza.core.presentation.components.LazyPizzaAppBar
import com.joshayoung.lazypizza.core.presentation.components.LazyPizzaScaffold
import com.joshayoung.lazypizza.core.presentation.models.ImageResource
import com.joshayoung.lazypizza.search.data.mappers.toJson
import com.joshayoung.lazypizza.search.data.models.Products
import com.joshayoung.lazypizza.search.presentation.components.SearchField
import com.joshayoung.lazypizza.search.presentation.models.ProductUi
import com.joshayoung.lazypizza.ui.theme.LazyPizzaTheme
import io.appwrite.extensions.toJson
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel = koinViewModel(),
    goToDetails: (product: String) -> Unit
) {
    HomeScreen(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        goToDetails = goToDetails
    )
}

@Composable
fun HomeScreen(
    state: HomeState,
    goToDetails: (product: String) -> Unit
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
                if (state.noItemsFound) {
                    Box(
                        modifier =
                            Modifier
                                .padding(top = 20.dp)
                                .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            style = MaterialTheme.typography.bodyMedium,
                            text = "No results found for your query",
                            modifier = Modifier
                        )
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        state.items.forEach { iii ->
                            stickyHeader {
                                Text(iii.name.first().titlecase() + iii.name.substring(1))
                            }
                            items(iii.items) { product ->
                                ItemAndPrice(product, goToDetails = goToDetails)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemAndPrice(
    productUi: ProductUi,
    goToDetails: (product: String) -> Unit
) {
    val inPreviewOrDebug = LocalInspectionMode.current || BuildConfig.DEBUG
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
                    goToDetails(productUi.toJson())
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
                if (inPreviewOrDebug) {
                    ImageResource.DrawableResource(productUi.imageResource)
                } else {
                    ImageResource.RemoteFilePath(productUi.remoteImageUrl)
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
                Text(productUi.name, fontWeight = FontWeight.Bold)
                Text(productUi.description ?: "")
                Text(productUi.price, style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchItemsScreenPreview() {
    LazyPizzaTheme {
        HomeScreen(
            state =
                HomeState(
//                    noItemsFound = true,
                    items =
                        listOf(
                            Products(
                                name = "Pizzas",
                                items =
                                    listOf(
                                        ProductUi(
                                            description = "A delicious food",
                                            imageResource = R.drawable.hawaiian,
                                            name = "Hawaiian Pizza",
                                            price = "$10.19"
                                        ),
                                        ProductUi(
                                            description = "Another food",
                                            imageResource = R.drawable.meat_lovers,
                                            name = "Meat Lovers Pizza",
                                            price = "$12.98"
                                        )
                                    )
                            ),
                            Products(
                                name = "Ice Cream",
                                items =
                                    listOf(
                                        ProductUi(
                                            description = "A delicious food",
                                            imageResource = R.drawable.cookies,
                                            name = "Hawaiian Pizza",
                                            price = "$10.19"
                                        ),
                                        ProductUi(
                                            description = "Another food",
                                            imageResource = R.drawable.strawberry,
                                            name = "Meat Lovers Pizza",
                                            price = "$12.98"
                                        )
                                    )
                            ),
                            Products(
                                name = "Drinks",
                                items =
                                    listOf(
                                        ProductUi(
                                            description = "A delicious food",
                                            imageResource = R.drawable.mineral_water,
                                            name = "Hawaiian Pizza",
                                            price = "$10.19"
                                        ),
                                        ProductUi(
                                            description = "Another food",
                                            imageResource = R.drawable.pepsi,
                                            name = "Meat Lovers Pizza",
                                            price = "$12.98"
                                        )
                                    )
                            ),
                            Products(
                                name = "Sauces",
                                items =
                                    listOf(
                                        ProductUi(
                                            description = "A delicious food",
                                            imageResource = R.drawable.spicy_chili_sauce,
                                            name = "Hawaiian Pizza",
                                            price = "$10.19"
                                        ),
                                        ProductUi(
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
