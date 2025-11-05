package com.joshayoung.lazypizza.history.presentation.order_history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import com.joshayoung.lazypizza.core.presentation.components.LargePizzaScaffold
import com.joshayoung.lazypizza.core.presentation.components.PizzaAppBar
import com.joshayoung.lazypizza.core.presentation.components.PizzaBottomBar
import com.joshayoung.lazypizza.core.presentation.components.SmallPizzaScaffold
import com.joshayoung.lazypizza.core.presentation.models.BottomNavItemUi
import com.joshayoung.lazypizza.core.presentation.utils.previewBottomNavItemUis
import com.joshayoung.lazypizza.core.presentation.utils.previewOrders
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.core.ui.theme.surfaceHighest
import com.joshayoung.lazypizza.core.utils.DeviceConfiguration
import com.joshayoung.lazypizza.history.domain.models.Order
import com.joshayoung.lazypizza.history.presentation.components.HistoryCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun HistoryScreenRoot(
    logOut: () -> Unit,
    isLoggedIn: Boolean,
    cartItems: Int,
    viewModel: HistoryViewModel = koinViewModel(),
    bottomNavItemUis: List<BottomNavItemUi>,
    goToLogin: () -> Unit
) {
    HistoryScreen(
        isLoggedIn = isLoggedIn,
        logOut = logOut,
        bottomNavItemUis = bottomNavItemUis,
        cartItems = cartItems,
        state = viewModel.state.collectAsStateWithLifecycle().value,
        goToLogin = goToLogin,
        onAction = { action ->
            viewModel.onAction(action)
        }
    )
}

@Composable
fun HistoryScreen(
    logOut: () -> Unit,
    isLoggedIn: Boolean,
    bottomNavItemUis: List<BottomNavItemUi>,
    cartItems: Int,
    state: HistoryState,
    goToLogin: () -> Unit,
    onAction: (HistoryAction) -> Unit
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> {
            SmallPizzaScaffold(
                topAppBar = {
                    PizzaAppBar(
                        isAuthenticated = isLoggedIn,
                        logOut = logOut,
                        showLogo = false,
                        showContact = false,
                        title = "Order History"
                    )
                },
                bottomBar = {
                    PizzaBottomBar(
                        cartItems = cartItems,
                        bottomNavItemUis = bottomNavItemUis
                    )
                }
            ) { innerPadding ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier =
                        Modifier
                            .background(MaterialTheme.colorScheme.surfaceHighest)
                            .padding(innerPadding)
                            .padding(top = 20.dp)
                            .fillMaxSize()
                            .padding(horizontal = 20.dp)
                ) {
                    if (isLoggedIn) {
                        OrderHistory(orders = state.orders)
                    } else {
                        SignedOut(
                            goToLogin = goToLogin
                        )
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
                cartItems = cartItems,
                title = "Order History",
                appBarItems = bottomNavItemUis
            ) { innerPadding ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier =
                        Modifier
                            .background(MaterialTheme.colorScheme.surfaceHighest)
                            .padding(innerPadding)
                            .padding(top = 20.dp)
                            .fillMaxSize()
                            .padding(horizontal = 20.dp)
                ) {
                    Text(
                        "Order History",
                        modifier =
                            Modifier
                                .padding(bottom = 20.dp)
                    )
                    if (isLoggedIn) {
                        OrderHistory(orders = state.orders)
                    } else {
                        SignedOut(
                            goToLogin = goToLogin
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrderHistory(orders: List<Order>) {
    LazyVerticalGrid(
        state = rememberLazyGridState(),
        columns = GridCells.Fixed(1)
    ) {
        items(orders) { order ->
            HistoryCard(
                order,
                modifier =
                    Modifier
                        .padding(bottom = 10.dp)
                        .height(100.dp)
            )
        }
    }
}

@Composable
fun SignedOut(goToLogin: () -> Unit) {
    Text(
        "Not Signed In",
        style = MaterialTheme.typography.titleLarge,
        modifier =
            Modifier
                .padding(top = 140.dp)
    )
    Text("Please sign in to view your order history")
    Button(onClick = {
        goToLogin()
    }) {
        Text("Sign In")
    }
}

@Preview
@Preview(
    showBackground = true,
    widthDp = 800,
    heightDp = 1280
)
@Composable
fun HistoryScreenPreview() {
    LazyPizzaTheme {
        HistoryScreen(
            state =
                HistoryState(
                    isSignedIn = true,
                    orders = previewOrders
                ),
            onAction = {},
            goToLogin = {},
            cartItems = 2,
            isLoggedIn = false,
            logOut = {},
            bottomNavItemUis = previewBottomNavItemUis
        )
    }
}