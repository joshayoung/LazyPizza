package com.joshayoung.lazypizza.cart.presentation.confirmation.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshayoung.lazypizza.core.presentation.components.RoundedTopBar
import com.joshayoung.lazypizza.core.presentation.components.SmallRoundedPizzaScaffold
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.core.ui.theme.surfaceHigher
import com.joshayoung.lazypizza.core.utils.DeviceConfiguration
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConfirmationScreenRoot(
    viewModel: ConfirmationViewModel = koinViewModel(),
    backToMain: () -> Unit
) {
    ConfirmationScreen(
        backToMain = backToMain,
        state = viewModel.state
    )
}

@Composable
fun ConfirmationScreen(
    state: ConfirmationState,
    backToMain: () -> Unit
) {
    val paddingVertical = 10.dp
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> {
            SmallRoundedPizzaScaffold(
                topPadding = 60.dp,
                topBar = {
                    RoundedTopBar()
                }
            ) { innerPadding ->

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier =
                        Modifier
                            .background(MaterialTheme.colorScheme.surfaceHigher)
                            .padding(innerPadding)
                            .fillMaxSize()
                            .padding(innerPadding)
                ) {
                    ConfirmationInfo(
                        paddingVertical = paddingVertical,
                        state = state,
                        backToMain = backToMain
                    )
                }
            }
        }

        DeviceConfiguration.MOBILE_LANDSCAPE -> {
        }

        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> {
            SmallRoundedPizzaScaffold(
                topPadding = 60.dp,
                topBar = {
                    RoundedTopBar()
                }
            ) { innerPadding ->

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier =
                        Modifier
                            .background(MaterialTheme.colorScheme.surfaceHigher)
                            .padding(innerPadding)
                            .fillMaxSize()
                            .padding(innerPadding)
                ) {
                    ConfirmationInfo(
                        maxWidth = 400.dp,
                        paddingVertical = paddingVertical,
                        state = state,
                        backToMain = backToMain
                    )
                }
            }
        }
    }

    if (state.isLoading) {
        Box(
            modifier =
                Modifier
                    .clickable {
                        // NOTE: Prevent clicking items below.
                    }.fillMaxSize()
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier =
                    Modifier
                        .size(80.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun ConfirmationInfo(
    paddingVertical: Dp,
    state: ConfirmationState,
    backToMain: () -> Unit,
    maxWidth: Dp? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            if (maxWidth != null) {
                Modifier.then(Modifier.widthIn(max = maxWidth))
            } else {
                Modifier
            }
    ) {
        Text(
            "Your order has been placed!",
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(paddingVertical)
        )
        Text(
            "Thank you for your order! Please come at the indicated time.",
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .padding(paddingVertical)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier =
                Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(10.dp)
                    ).padding(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier =
                    Modifier
                        .fillMaxWidth()
            ) {
                Text("Order Number:".uppercase())
                Text(state.orderNumber, style = MaterialTheme.typography.titleSmall)
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier =
                    Modifier
                        .fillMaxWidth()
            ) {
                Text("Pickup Time:".uppercase())
                Text(state.pickupTime, style = MaterialTheme.typography.titleSmall)
            }
        }

        TextButton(
            onClick = {
                backToMain()
            },
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text(
                "Back to Menu",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(
    showBackground = true,
    widthDp = 800,
    heightDp = 1280
)
@Composable
private fun ConfirmationScreenPreview() {
    LazyPizzaTheme {
        ConfirmationScreen(
            state =
                ConfirmationState(
                    isLoading = false
                ),
            backToMain = {}
        )
    }
}