package com.joshayoung.lazypizza.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.joshayoung.lazypizza.core.presentation.components.LazyPizzaAppBar
import com.joshayoung.lazypizza.core.presentation.components.LazyPizzaBottomBar
import com.joshayoung.lazypizza.core.presentation.components.LazyPizzaScaffold
import com.joshayoung.lazypizza.core.utils.DeviceConfiguration

@Composable
fun HistoryScreenRoot(
    navigateToDetails: () -> Unit,
    navigateToCart: () -> Unit,
    navigateToHistory: () -> Unit
) {
    HistoryScreen(
        navigateToDetails = navigateToDetails,
        navigateToCart = navigateToCart,
        navigateToHistory = navigateToHistory
    )
}

@Composable
fun HistoryScreen(
    navigateToDetails: () -> Unit,
    navigateToCart: () -> Unit,
    navigateToHistory: () -> Unit
) {
    Text(text = "history screen")
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    val coroutineScope = rememberCoroutineScope()

    when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> {
            LazyPizzaScaffold(
                topAppBar = { LazyPizzaAppBar() },
                bottomBar = {
                    LazyPizzaBottomBar(
                        menuClick = navigateToDetails,
                        cartClick = navigateToCart,
                        historyClick = navigateToHistory
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier =
                        Modifier
                            .padding(innerPadding)
                ) {
                    Text("History Screen")
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
                ) {
                    Text("History Screen")
                }
            }
        }
    }
}