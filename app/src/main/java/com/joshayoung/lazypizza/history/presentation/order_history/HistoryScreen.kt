package com.joshayoung.lazypizza.history.presentation.order_history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.joshayoung.lazypizza.cart.presentation.cart_list.CartViewModel
import com.joshayoung.lazypizza.core.presentation.components.LargePizzaScaffold
import com.joshayoung.lazypizza.core.presentation.components.PizzaAppBar
import com.joshayoung.lazypizza.core.presentation.components.PizzaBottomBar
import com.joshayoung.lazypizza.core.presentation.components.SmallPizzaScaffold
import com.joshayoung.lazypizza.core.presentation.models.BottomNavItemUi
import com.joshayoung.lazypizza.core.presentation.utils.previewBottomNavItemUis
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.core.utils.DeviceConfiguration
import org.koin.androidx.compose.koinViewModel

@Composable
fun HistoryScreenRoot(
    viewModel: HistoryViewModel = koinViewModel(),
    bottomNavItemUis: List<BottomNavItemUi>
) {
    HistoryScreen(
        bottomNavItemUis = bottomNavItemUis,
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onAction = { action ->
            viewModel.onAction(action)
        }
    )
}

@Composable
fun HistoryScreen(
    bottomNavItemUis: List<BottomNavItemUi>,
    state: HistoryState,
    onAction: (HistoryAction) -> Unit
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
                        title = "Order History"
                    )
                },
                bottomBar = {
                    PizzaBottomBar(
                        cartItems = state.cartItems,
                        bottomNavItemUis = bottomNavItemUis
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
                            .padding(top = 140.dp)
                ) {
                    Text("Not Signed In", style = MaterialTheme.typography.titleLarge)
                    Text("Please sign in to view your order history")
                    Button(onClick = {}) {
                        Text("Sign In")
                    }
                }
            }
        }
        DeviceConfiguration.MOBILE_LANDSCAPE -> {
        }
        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> {
            LargePizzaScaffold(
                cartItems = state.cartItems,
                appBarItems = bottomNavItemUis
            ) { innerPadding ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(innerPadding)
                            .padding(top = 140.dp)
                ) {
                    Text("Not Signed In", style = MaterialTheme.typography.titleLarge)
                    Text("Please sign in to view your order history")
                    Button(onClick = {}) {
                        Text("Sign In")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun HistoryScreenPreview() {
    LazyPizzaTheme {
        HistoryScreen(
            state = HistoryState(),
            onAction = {},
            bottomNavItemUis = previewBottomNavItemUis
        )
    }
}