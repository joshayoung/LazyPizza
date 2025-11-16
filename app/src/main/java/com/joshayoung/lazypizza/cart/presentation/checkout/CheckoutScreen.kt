package com.joshayoung.lazypizza.cart.presentation.checkout

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joshayoung.lazypizza.cart.presentation.components.CartItem
import com.joshayoung.lazypizza.cart.presentation.components.RecommendedAddOns
import com.joshayoung.lazypizza.core.presentation.components.SmallPizzaScaffold
import com.joshayoung.lazypizza.core.presentation.components.TopBar
import com.joshayoung.lazypizza.core.presentation.utils.addOnsForPreview
import com.joshayoung.lazypizza.core.presentation.utils.inCartItemsForPreviewUis
import com.joshayoung.lazypizza.core.ui.theme.DownIcon
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.core.utils.DeviceConfiguration
import org.koin.androidx.compose.koinViewModel

@Composable
fun CheckoutScreenRoot(
    viewModel: CheckoutViewModel = koinViewModel(),
    backToCart: () -> Unit
) {
    CheckoutScreen(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onAction = { action ->
            viewModel.onAction(action)
        },
        backToCart = backToCart
    )
}

@Composable
fun CheckoutScreen(
    state: CheckoutState,
    onAction: (CheckoutAction) -> Unit,
    backToCart: () -> Unit
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> {
            SmallPizzaScaffold(
                topAppBar = {
                    TopBar(
                        showLogo = false,
                        showBackButton = true,
                        onBackClick = {
                            backToCart()
                        },
                        showContact = false,
                        title = "Order Checkout"
                    )
                }
            ) { innerPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(bottom = 100.dp)
                    ) {
                        item {
                            Column(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                            ) {
                                Text(text = "Pickup Time".uppercase())
                                TimeSelection(
                                    onSelect = {},
                                    isSelected = false,
                                    text = "Earliest available time"
                                )
                                TimeSelection(
                                    onSelect = {},
                                    isSelected = false,
                                    text = "Schedule time"
                                )
                            }
                        }
                        item {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Order Details".uppercase())
                                IconButton(
                                    onClick = {
                                    },
                                    modifier =
                                        Modifier
                                            .border(
                                                1.dp,
                                                color = MaterialTheme.colorScheme.outlineVariant,
                                                shape = RoundedCornerShape(8.dp)
                                            ).padding(6.dp)
                                            .size(20.dp)
                                ) {
                                    Icon(
                                        imageVector = DownIcon,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier =
                                        Modifier
                                    )
                                }
                            }
                        }

                        items(state.items, key = { it.key }) { inCartItem ->
                            CartItem(
                                inCartItem,
                                modifier =
                                    Modifier
                                        .padding(bottom = 10.dp)
                                        .height(140.dp),
                                removeAllFromCart = { inCartItemUi ->

                                    onAction(CheckoutAction.RemoveAllFromCart(inCartItemUi))
                                },
                                removeItemFromCart = { inCartItemUi ->
                                    onAction(
                                        CheckoutAction.RemoveItemFromCart(inCartItemUi)
                                    )
                                },
                                addItemToCart = { inCartItemUi ->
                                    onAction(CheckoutAction.AddItemToCart(inCartItemUi))
                                }
                            )
                        }
                        item {
                            RecommendedAddOns(
                                state.recommendedAddOns,
                                addProductToCart = {
                                    onAction(CheckoutAction.AddAddOnToCart(it))
                                }
                            )
                        }
                    }
                    Column(
                        modifier =
                            Modifier
                                .align(Alignment.BottomCenter)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {
                            Text("Order Total")
                            Text("$25.34")
                        }
                        PlaceOrderButton2()
                    }
                }
            }
        }
        DeviceConfiguration.MOBILE_LANDSCAPE -> {
        }
        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> {
        }
    }

    // /
}

@Composable
fun Accordion(
    state: CheckoutState,
    onAction: (CheckoutAction) -> Unit,
    modifier: Modifier = Modifier
) {
    var isOpen by remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = "Order Details".uppercase())
        IconButton(
            onClick = {
                isOpen = !isOpen
            },
            modifier =
                Modifier
                    .border(
                        1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(8.dp)
                    ).padding(6.dp)
                    .size(20.dp)
        ) {
            Icon(
                imageVector = DownIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier =
                Modifier
            )
        }
    }

//    AnimatedVisibility(
//        visible = isOpen,
//        enter = slideInVertically(initialOffsetY = { -it }),
//        exit = slideOutVertically(targetOffsetY = { -it })
//    ) {
    Column {
        LazyVerticalGrid(
            modifier =
            Modifier,
            columns = GridCells.Fixed(1)
        ) {
            items(state.items, key = { it.key }) { inCartItem ->
                CartItem(
                    inCartItem,
                    modifier =
                        Modifier
                            .padding(bottom = 10.dp)
                            .height(140.dp),
                    removeAllFromCart = { inCartItemUi ->

                        onAction(CheckoutAction.RemoveAllFromCart(inCartItemUi))
                    },
                    removeItemFromCart = { inCartItemUi ->
                        onAction(
                            CheckoutAction.RemoveItemFromCart(inCartItemUi)
                        )
                    },
                    addItemToCart = { inCartItemUi ->
                        onAction(CheckoutAction.AddItemToCart(inCartItemUi))
                    }
                )
            }
        }
    }
//    }
}

@Composable
fun PlaceOrderButton2() {
    Button(onClick = {}) {
        Text(
            "Place Order",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TimeSelection(
    text: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            Modifier
                .border(
                    1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(100.dp)
                ).padding(
                    start = 10.dp,
                    end = 20.dp
                ).fillMaxWidth()
    ) {
        RadioButton(onClick = {
            onSelect()
        }, selected = isSelected)
        Text(text)
    }
}

@Preview(showBackground = true)
@Composable
private fun CheckoutScreenPreview() {
    LazyPizzaTheme {
        CheckoutScreen(
            backToCart = {},
            state =
                CheckoutState(
                    items = inCartItemsForPreviewUis,
                    recommendedAddOns = addOnsForPreview
                ),
            onAction = {}
        )
    }
}