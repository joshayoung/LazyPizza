package com.joshayoung.lazypizza.order.presentation.order_history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joshayoung.lazypizza.core.presentation.components.LargePizzaScaffold
import com.joshayoung.lazypizza.core.presentation.components.PizzaBottomBar
import com.joshayoung.lazypizza.core.presentation.components.SmallPizzaScaffold
import com.joshayoung.lazypizza.core.presentation.components.TopBar
import com.joshayoung.lazypizza.core.presentation.models.BottomNavItemUi
import com.joshayoung.lazypizza.core.presentation.utils.previewBottomNavItemUis
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.core.utils.DeviceConfiguration
import com.joshayoung.lazypizza.order.presentation.components.OrderCard
import com.joshayoung.lazypizza.order.presentation.models.OrderUi
import org.koin.androidx.compose.koinViewModel

@Composable
fun OrderScreenRoot(
    isLoggedIn: Boolean,
    cartItems: Int,
    viewModel: OrderViewModel = koinViewModel(),
    bottomNavItemUis: List<BottomNavItemUi>,
    goToMenu: () -> Unit,
    goToLogin: () -> Unit
) {
    OrderScreen(
        isLoggedIn = isLoggedIn,
        bottomNavItemUis = bottomNavItemUis,
        cartItems = cartItems,
        state = viewModel.state.collectAsStateWithLifecycle().value,
        goToMenu = goToMenu,
        goToLogin = goToLogin
    )
}

@Composable
fun OrderScreen(
    isLoggedIn: Boolean,
    bottomNavItemUis: List<BottomNavItemUi>,
    cartItems: Int,
    state: OrderState,
    goToMenu: () -> Unit,
    goToLogin: () -> Unit
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> {
            SmallPizzaScaffold(
                topAppBar = {
                    TopBar(
                        isAuthenticated = isLoggedIn,
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
                            .padding(innerPadding)
                            .padding(top = 20.dp)
                            .fillMaxSize()
                            .padding(horizontal = 20.dp)
                ) {
                    if (isLoggedIn) {
                        OrderHistory(orderUis = state.orderUis, goToMenu = goToMenu)
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
                appBarItems = bottomNavItemUis
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier =
                        Modifier
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
                        OrderHistory(orderUis = state.orderUis, columns = 2, goToMenu = goToMenu)
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
fun OrderHistory(
    orderUis: List<OrderUi>,
    columns: Int = 1,
    goToMenu: () -> Unit
) {
    if (orderUis.isEmpty()) {
        EmptyOrderList(goToMenu = goToMenu)
    } else {
        OrderList(orderUis = orderUis, columns = columns)
    }
}

@Composable
fun OrderList(
    orderUis: List<OrderUi>,
    columns: Int
) {
    LazyVerticalStaggeredGrid(
        state = rememberLazyStaggeredGridState(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalItemSpacing = 4.dp,
        columns = StaggeredGridCells.Fixed(columns),
        modifier =
            Modifier
                .fillMaxSize()
    ) {
        items(orderUis) { orderUi ->
            OrderCard(orderUi)
        }
    }
}

@Composable
fun EmptyOrderList(goToMenu: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "No Orders Yet",
            style = MaterialTheme.typography.titleLarge,
            modifier =
                Modifier
                    .padding(top = 140.dp)
        )
        Text(
            "Your orders will appear here after your first purchase.",
            textAlign =
                TextAlign.Center
        )
        Button(onClick = {
            goToMenu()
        }) {
            Text("Go to Menu")
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
    Text("Please sign in to view your order history.")
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
fun OrderScreenPreview() {
    LazyPizzaTheme {
        OrderScreen(
            state =
                OrderState(
                    isSignedIn = true,
                    orderUis = emptyList() // previewOrders
                ),
            goToMenu = {},
            cartItems = 2,
            isLoggedIn = true,
            goToLogin = {},
            bottomNavItemUis = previewBottomNavItemUis
        )
    }
}