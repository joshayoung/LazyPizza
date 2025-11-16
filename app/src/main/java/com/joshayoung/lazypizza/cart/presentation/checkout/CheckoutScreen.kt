package com.joshayoung.lazypizza.cart.presentation.checkout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joshayoung.lazypizza.cart.presentation.components.CartItem
import com.joshayoung.lazypizza.cart.presentation.components.RecommendedAddOns
import com.joshayoung.lazypizza.core.presentation.components.SmallPizzaScaffold
import com.joshayoung.lazypizza.core.presentation.components.TopBar
import com.joshayoung.lazypizza.core.presentation.models.InCartItemUi
import com.joshayoung.lazypizza.core.presentation.utils.addOnsForPreview
import com.joshayoung.lazypizza.core.presentation.utils.inCartItemsForPreviewUis
import com.joshayoung.lazypizza.core.ui.theme.DownIcon
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.core.ui.theme.UpIcon
import com.joshayoung.lazypizza.core.ui.theme.surfaceHighest
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
    val isOpen = remember { mutableStateOf(false) }
    val pagePadding = 10.dp
    val verticalPadding = 10.dp

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
                                .padding(pagePadding)
                    ) {
                        item {
                            TimeSelections(
                                modifier =
                                    Modifier
                                        .padding(vertical = verticalPadding)
                            )
                        }

                        item {
                            EarliestTime(
                                modifier =
                                    Modifier
                                        .padding(vertical = verticalPadding)
                            )
                        }

                        item {
                            CheckoutDivider()
                        }

                        item {
                            AccordionHeader(
                                isOpen,
                                modifier =
                                    Modifier
                                        .padding(vertical = verticalPadding)
                            )
                        }

                        items(state.items, key = { it.key }) { inCartItem ->
                            Accordion(inCartItem, onAction = onAction, isOpen)
                        }

                        item {
                            CheckoutDivider()
                        }

                        item {
                            RecommendedAddOns(
                                modifier =
                                    Modifier
                                        .padding(vertical = verticalPadding),
                                addOns = state.recommendedAddOns,
                                addProductToCart = {
                                    onAction(CheckoutAction.AddAddOnToCart(it))
                                }
                            )
                        }

                        item {
                            CheckoutDivider()
                        }

                        item {
                            Comments(
                                modifier =
                                    Modifier
                                        .padding(vertical = verticalPadding)
                            )
                        }
                    }

                    Footer(
                        modifier =
                            Modifier
                                .padding(pagePadding)
                                .align(Alignment.BottomCenter)
                    )
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
}

@Composable
fun TimeSelections(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier =
            modifier
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

@Composable
fun EarliestTime(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Earliest Pickup Time".uppercase())
        Text("12:15", style = MaterialTheme.typography.titleSmall)
    }
}

@Composable
fun AccordionHeader(
    isOpen: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier =
        modifier
    ) {
        Text(text = "Order Details".uppercase())
        IconButton(
            onClick = {
                isOpen.value = !isOpen.value
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
            val icon = if (isOpen.value) UpIcon else DownIcon
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun CheckoutDivider() {
    HorizontalDivider(
        modifier =
            Modifier
                .padding(vertical = 10.dp)
                .background(MaterialTheme.colorScheme.outline)
    )
}

@Composable
fun Comments(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
    ) {
        Text("Comments".uppercase(), style = MaterialTheme.typography.labelMedium)
        TextField(
            colors =
                TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
            label = {
                Text("Add Comment")
            },
            lineLimits = TextFieldLineLimits.MultiLine(3),
            modifier =
                Modifier
                    .background(
                        MaterialTheme.colorScheme.surfaceHighest,
                        shape =
                            RoundedCornerShape(20.dp)
                    ).fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            state = TextFieldState()
        )
    }
}

@Composable
fun Footer(modifier: Modifier = Modifier) {
    Column(
        modifier =
        modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier =
                Modifier.fillMaxWidth()
        ) {
            Text("Order Total:".uppercase(), style = MaterialTheme.typography.bodySmall)
            Text("$25.34", style = MaterialTheme.typography.titleSmall)
        }
        PlaceOrderButton2()
    }
}

@Composable
fun Accordion(
    inCartItem: InCartItemUi,
    onAction: (CheckoutAction) -> Unit,
    isOpen: MutableState<Boolean>
) {
    AnimatedVisibility(
        visible = isOpen.value,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it })
    ) {
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