package com.joshayoung.lazypizza.history

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
fun HistoryScreenRoot(bottomNavItems: List<BottomNavItem>) {
    HistoryScreen(
        bottomNavItems = bottomNavItems
    )
}

@Composable
fun HistoryScreen(bottomNavItems: List<BottomNavItem>) {
    Text(text = "history screen")
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> {
            LazyPizzaScaffold(
                topAppBar = {
                    LazyPizzaAppBar(
                        showLogo = false,
                        showContact = false,
                        title = "Order History"
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
            NavigationRailScaffold(
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
            bottomNavItems = previewBottomNavItems
        )
    }
}