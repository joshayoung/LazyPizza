package com.joshayoung.lazypizza.cart

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
import com.joshayoung.lazypizza.core.presentation.components.LazyPizzaAppBar
import com.joshayoung.lazypizza.core.presentation.components.LazyPizzaBottomBar
import com.joshayoung.lazypizza.core.presentation.components.LazyPizzaScaffold
import com.joshayoung.lazypizza.core.presentation.components.NavigationRailScaffold
import com.joshayoung.lazypizza.core.presentation.utils.previewBottomNavItems
import com.joshayoung.lazypizza.core.utils.BottomNavItem
import com.joshayoung.lazypizza.core.utils.DeviceConfiguration
import com.joshayoung.lazypizza.ui.theme.LazyPizzaTheme

@Composable
fun CartScreenRoot(bottomNavItems: List<BottomNavItem>) {
    CartScreen(
        bottomNavItems = bottomNavItems
    )
}

@Composable
fun CartScreen(bottomNavItems: List<BottomNavItem>) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> {
            LazyPizzaScaffold(
                topAppBar = {
                    LazyPizzaAppBar(
                        showLogo = false,
                        showContact = false,
                        title = "Cart"
                    )
                },
                bottomBar = {
                    LazyPizzaBottomBar(
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
                            .padding(top = 140.dp)
                ) {
                    Text("Your Cart Is Empty", style = MaterialTheme.typography.titleLarge)
                    Text("Head back to the menu nd grab a pizza you love.")
                    Button(onClick = {}) {
                        Text("Back to Menu")
                    }
                }
            }
        }
        DeviceConfiguration.MOBILE_LANDSCAPE -> {
        }
        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> {
            NavigationRailScaffold(
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
                            .padding(top = 140.dp)
                ) {
                    Text("Your Cart Is Empty", style = MaterialTheme.typography.titleLarge)
                    Text("Head back to the menu nd grab a pizza you love.")
                    Button(onClick = {}) {
                        Text("Back to Menu")
                    }
                }
            }
        }
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
        CartScreen(
            bottomNavItems = previewBottomNavItems
        )
    }
}