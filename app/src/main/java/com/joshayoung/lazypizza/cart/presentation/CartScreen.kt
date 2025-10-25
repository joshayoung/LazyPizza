package com.joshayoung.lazypizza.cart.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.joshayoung.lazypizza.core.presentation.components.LargePizzaScaffold
import com.joshayoung.lazypizza.core.presentation.components.PizzaAppBar
import com.joshayoung.lazypizza.core.presentation.components.PizzaBottomBar
import com.joshayoung.lazypizza.core.presentation.components.SideItem
import com.joshayoung.lazypizza.core.presentation.components.SmallPizzaScaffold
import com.joshayoung.lazypizza.core.presentation.models.BottomNavItem
import com.joshayoung.lazypizza.core.presentation.utils.previewBottomNavItems
import com.joshayoung.lazypizza.core.presentation.utils.productUiListForPreview
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.core.utils.DeviceConfiguration
import org.koin.androidx.compose.koinViewModel

@Composable
fun CartScreenRoot(
    viewModel: CartViewModel = koinViewModel(),
    bottomNavItems: List<BottomNavItem>
) {
    CartScreen(
        bottomNavItems = bottomNavItems,
        state = viewModel.state.collectAsStateWithLifecycle().value
    )
}

@Composable
fun CartScreen(
    bottomNavItems: List<BottomNavItem>,
    state: CartState
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> {
            SmallPizzaScaffold(
                topAppBar = {
                    PizzaAppBar(
                        showLogo = false,
                        showContact = false,
                        title = "Cart"
                    )
                },
                bottomBar = {
                    PizzaBottomBar(
                        bottomNavItems = bottomNavItems
                    )
                }
            ) { innerPadding ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(innerPadding)
                ) {
                    CartList(state)
                }
            }
        }
        DeviceConfiguration.MOBILE_LANDSCAPE -> {
        }
        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> {
            LargePizzaScaffold(
                title = "Cart",
                appBarItems = bottomNavItems
            ) { innerPadding ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(innerPadding)
                ) {
                    CartList(state)
                }
            }
        }
    }
}

@Composable
fun CartList(state: CartState) {
    if (state.items.count() < 1) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier
                    .padding(top = 140.dp)
        ) {
            Text("Your Cart Is Empty", style = MaterialTheme.typography.titleLarge)
            Text("Head back to the menu nd grab a pizza you love.")
            Button(onClick = {}) {
                Text("Back to Menu")
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(1)
        ) {
            items(state.items) { productUi ->
                SideItem(
                    productUi,
                    modifier =
                        Modifier
                            .height(120.dp)
                ) { }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
// @Preview(
//    showBackground = true,
//    widthDp = 800,
//    heightDp = 1280
// )
private fun CartScreenPreview() {
    LazyPizzaTheme {
        CartScreen(
            bottomNavItems = previewBottomNavItems,
            state =
                CartState(
                    items = productUiListForPreview
                )
        )
    }
}