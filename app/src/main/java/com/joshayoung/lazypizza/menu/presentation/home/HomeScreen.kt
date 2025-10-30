package com.joshayoung.lazypizza.menu.presentation.home

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
import com.joshayoung.lazypizza.core.presentation.components.LargePizzaScaffold
import com.joshayoung.lazypizza.core.presentation.components.PizzaAppBar
import com.joshayoung.lazypizza.core.presentation.components.PizzaBottomBar
import com.joshayoung.lazypizza.core.presentation.components.SmallPizzaScaffold
import com.joshayoung.lazypizza.core.presentation.models.BottomNavItem
import com.joshayoung.lazypizza.core.presentation.utils.previewBottomNavItems
import com.joshayoung.lazypizza.core.presentation.utils.previewProducts
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.core.utils.DeviceConfiguration
import com.joshayoung.lazypizza.menu.presentation.components.SearchField
import com.joshayoung.lazypizza.menu.presentation.components.SideItem
import com.joshayoung.lazypizza.menu.presentation.home.components.ProductItem
import com.joshayoung.lazypizza.menu.presentation.models.MenuType
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.getValue

@OptIn(FlowPreview::class)
@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel = koinViewModel(),
    goToDetails: (id: String) -> Unit,
    bottomNavItems: List<BottomNavItem>
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
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
        goToDetails = goToDetails,
        lazyGridState = listState,
        bottomNavItems = bottomNavItems,
        state = state,
        onAction = { action ->
            viewModel.onAction(action)
        }
    )
}

@Composable
fun HomeScreen(
    state: HomeState,
    goToDetails: (id: String) -> Unit,
    lazyGridState: LazyGridState,
    bottomNavItems: List<BottomNavItem>,
    onAction: (HomeAction) -> Unit
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    val coroutineScope = rememberCoroutineScope()

    when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> {
            SmallPizzaScaffold(
                topAppBar = { PizzaAppBar() },
                bottomBar = {
                    PizzaBottomBar(
                        bottomNavItems = bottomNavItems,
                        cartItems = state.cartItems
                    )
                }
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
                    ProductItems(
                        lazyGridState,
                        state,
                        goToDetails = goToDetails,
                        1,
                        onAction = onAction
                    )
                }
            }
        }
        DeviceConfiguration.MOBILE_LANDSCAPE -> {
        }
        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> {
            LargePizzaScaffold(
                appBarItems = bottomNavItems,
                cartItems = state.cartItems
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
                    ProductItems(
                        lazyGridState,
                        state,
                        goToDetails = goToDetails,
                        2,
                        onAction = onAction
                    )
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
    val options = state.items
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
                        lazyGridState.animateScrollToItem(label.startingIndex)
                    }
                },
                label = { Text(label.menuType.chipValue) },
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
    columns: Int,
    onAction: (HomeAction) -> Unit
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
                    state.items.forEach { groupedItem ->
                        if (groupedItem.products.count() > 0) {
                            stickyHeader {
                                Text(
                                    modifier =
                                        Modifier.background(
                                            MaterialTheme.colorScheme.background
                                        ),
                                    text = groupedItem.menuType.displayValue,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        items(groupedItem.products) {
                            ItemAndPrice(
                                it,
                                goToDetails = goToDetails,
                                modifier = Modifier.height(130.dp),
                                onAction = onAction
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
    modifier: Modifier = Modifier,
    onAction: (HomeAction) -> Unit
) {
    if (productUi.type == MenuType.Entree) {
        ProductItem(productUi, goToDetails = goToDetails, modifier = modifier)
    } else {
        SideItem(
            productUi,
            modifier = modifier,
            onAction = onAction
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchItemsScreenPreview() {
    LazyPizzaTheme {
        HomeScreen(
            state =
                HomeState(
//                    noItemsFound = true,
//                    isLoadingProducts = true,
                    items = previewProducts
                ),
            goToDetails = {},
            lazyGridState = LazyGridState(),
            bottomNavItems = previewBottomNavItems,
            onAction = {}
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
@Preview(
    showBackground = true,
    widthDp = 800,
    heightDp = 1280
)
private fun CartScreenPreview() {
    LazyPizzaTheme {
        HomeScreen(
            state =
                HomeState(
//                    noItemsFound = true,
//                    isLoadingProducts = true,
                    items = previewProducts
                ),
            goToDetails = {},
            lazyGridState = LazyGridState(),
            bottomNavItems = previewBottomNavItems,
            onAction = {}
        )
    }
}
