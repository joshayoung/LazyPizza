package com.joshayoung.lazypizza.search.presentation.home

import android.content.Context.MODE_PRIVATE
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.core.presentation.components.LazyPizzaAppBar
import com.joshayoung.lazypizza.core.presentation.components.LazyPizzaScaffold
import com.joshayoung.lazypizza.core.utils.DeviceConfiguration
import com.joshayoung.lazypizza.search.data.models.Products
import com.joshayoung.lazypizza.search.presentation.components.SearchField
import com.joshayoung.lazypizza.search.presentation.home.components.MultipleProductItem
import com.joshayoung.lazypizza.search.presentation.home.components.ProductItem
import com.joshayoung.lazypizza.search.presentation.models.ProductType
import com.joshayoung.lazypizza.search.presentation.models.ProductUi
import com.joshayoung.lazypizza.ui.theme.LazyPizzaTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.math.BigDecimal
import kotlin.getValue

@OptIn(FlowPreview::class)
@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel = koinViewModel(),
    goToDetails: (id: String) -> Unit
) {
    val applicationContext = LocalContext.current.applicationContext

    val prefs by lazy {
        applicationContext.getSharedPreferences("prefs", MODE_PRIVATE)
    }
    val scrollPosition = prefs.getInt("scroll_position", 0)

    val lazyGridState =
        rememberLazyGridState(
            initialFirstVisibleItemIndex = scrollPosition
        )

    LaunchedEffect(lazyGridState) {
        snapshotFlow {
            lazyGridState.firstVisibleItemIndex
        }.debounce(500L)
            .collectLatest { index ->
                prefs.edit {
                    putInt("scroll_position", index)
                        .apply()
                }
            }
    }
    val listState = remember { lazyGridState }

    HomeScreen(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        goToDetails = goToDetails,
        lazyGridState = listState
    )
}

@Composable
fun HomeScreen(
    state: HomeState,
    goToDetails: (id: String) -> Unit,
    lazyGridState: LazyGridState
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    val coroutineScope = rememberCoroutineScope()

    when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> {
            LazyPizzaScaffold(
                topAppBar = { LazyPizzaAppBar() }
            ) { innerPadding ->
                Column(
                    modifier =
                        Modifier
                            .padding(innerPadding)
                            .background(Color(0xFFFAFBFC))
                            .padding(horizontal = 20.dp)
                ) {
                    HeaderAndSearch(state, 150.dp)
                    Chips(lazyGridState, coroutineScope, state)
                    ProductItems(lazyGridState, state, goToDetails = goToDetails, 1)
                }
            }
        }
        DeviceConfiguration.MOBILE_LANDSCAPE -> {
        }
        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> {
            LazyPizzaScaffold(
                topAppBar = { LazyPizzaAppBar() }
            ) { innerPadding ->
                Column(
                    modifier =
                        Modifier
                            .padding(innerPadding)
                            .background(Color(0xFFFAFBFC))
                            .padding(horizontal = 20.dp)
                ) {
                    HeaderAndSearch(state, 140.dp)
                    Chips(lazyGridState, coroutineScope, state)
                    ProductItems(lazyGridState, state, goToDetails = goToDetails, 2)
                }
            }
        }
    }
}

@Composable
fun HeaderAndSearch(
    state: HomeState,
    height: Dp
) {
    Image(
        painterResource(id = R.drawable.pizza_header),
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier =
            Modifier
                .fillMaxWidth()
                .height(height = height)
    )
    SearchField(state.search, modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp))
}

@Composable
fun Chips(
    lazyGridState: LazyGridState,
    coroutineScope: CoroutineScope,
    state: HomeState
) {
    val options = listOf("Pizza", "Drinks", "Sauces", "Ice Cream")
    val selectedIndex by remember { mutableIntStateOf(-1) }
    Row {
        options.forEachIndexed { index, label ->
            val selected = index == selectedIndex
            AssistChip(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier =
                    Modifier
                        .padding(end = 4.dp),
                onClick = {
                    coroutineScope.launch {
                        if (index == 0) {
                            lazyGridState.animateScrollToItem(state.pizzaScrollPosition)
                        }
                        if (index == 1) {
                            lazyGridState.animateScrollToItem(state.drinkScrollPosition)
                        }
                        if (index == 2) {
                            lazyGridState.animateScrollToItem(state.sauceScrollPosition)
                        }
                        if (index == 3) {
                            lazyGridState.animateScrollToItem(state.iceCreamScrollPosition)
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
}

@Composable
fun ProductItems(
    lazyGridState: LazyGridState,
    state: HomeState,
    goToDetails: (String) -> Unit,
    columns: Int
) {
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
            if (state.isLoadingProducts) {
                Box(
                    modifier =
                        Modifier
                            .padding(top = 40.dp)
                            .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    state = lazyGridState,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.items.forEach { iii ->
                        stickyHeader {
                            Text(iii.name.uppercase(), style = MaterialTheme.typography.bodySmall)
                        }
                        items(iii.items) { product ->
                            ItemAndPrice(
                                product,
                                goToDetails = goToDetails,
                                modifier = Modifier.height(130.dp)
                            )
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
    goToDetails: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (productUi.type == ProductType.ENTRE) {
        ProductItem(productUi, goToDetails = goToDetails, modifier = modifier)
    } else {
        MultipleProductItem(productUi, modifier = modifier)
    }
}

@Preview(showBackground = true, showSystemUi = true)
// @Preview(
//    showBackground = true,
//    widthDp = 840,
//    heightDp = 360,
// )
// @Preview(
//    showBackground = true,
//    widthDp = 800,
//    heightDp = 1280
// )
@Composable
fun SearchItemsScreenPreview() {
    LazyPizzaTheme {
        HomeScreen(
            state =
                HomeState(
//                    noItemsFound = true,
//                    isLoadingProducts = true,
                    items =
                        listOf(
                            Products(
                                name = "Pizzas",
                                items =
                                    listOf(
                                        ProductUi(
                                            id = "1",
                                            description = "A delicious food",
                                            imageResource = R.drawable.hawaiian,
                                            name = "Hawaiian Pizza",
                                            price = BigDecimal("10.19"),
                                            type = ProductType.ENTRE
                                        ),
                                        ProductUi(
                                            id = "2",
                                            description =
                                                "Tomato sauce, mozzarella, " +
                                                    "mushrooms, olives, bell pepper, onion, corn",
                                            imageResource = R.drawable.meat_lovers,
                                            name = "Veggie Delight",
                                            price = BigDecimal("9.79"),
                                            type = ProductType.ENTRE
                                        )
                                    )
                            ),
                            Products(
                                name = "Ice Cream",
                                items =
                                    listOf(
                                        ProductUi(
                                            id = "3",
                                            description = "A delicious food",
                                            imageResource = R.drawable.cookies,
                                            name = "Hawaiian Pizza",
                                            price = BigDecimal("10.19")
                                        ),
                                        ProductUi(
                                            id = "4",
                                            description = "Another food",
                                            imageResource = R.drawable.strawberry,
                                            name = "Meat Lovers Pizza",
                                            price = BigDecimal("13.28")
                                        )
                                    )
                            ),
                            Products(
                                name = "Drinks",
                                items =
                                    listOf(
                                        ProductUi(
                                            id = "5",
                                            description = "A delicious food",
                                            imageResource = R.drawable.mineral_water,
                                            name = "Hawaiian Pizza",
                                            price = BigDecimal("8.18")
                                        ),
                                        ProductUi(
                                            id = "6",
                                            description = "Another food",
                                            imageResource = R.drawable.pepsi,
                                            name = "Meat Lovers Pizza",
                                            price = BigDecimal("18.88")
                                        )
                                    )
                            ),
                            Products(
                                name = "Sauces",
                                items =
                                    listOf(
                                        ProductUi(
                                            id = "7",
                                            description = "A delicious food",
                                            imageResource = R.drawable.spicy_chili_sauce,
                                            name = "Hawaiian Pizza",
                                            price = BigDecimal("21.19")
                                        ),
                                        ProductUi(
                                            id = "8",
                                            description = "Another food",
                                            imageResource = R.drawable.bbq_sauce,
                                            name = "Meat Lovers Pizza",
                                            price = BigDecimal("5.43")
                                        )
                                    )
                            )
                        )
                ),
            goToDetails = {},
            lazyGridState = LazyGridState()
        )
    }
}
