package com.joshayoung.lazypizza.cart.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.provider.FontsContractCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.util.TableInfo
import com.joshayoung.lazypizza.cart.presentation.components.AddOn
import com.joshayoung.lazypizza.cart.presentation.components.CartItem
import com.joshayoung.lazypizza.core.presentation.components.LargePizzaScaffold
import com.joshayoung.lazypizza.core.presentation.components.PizzaAppBar
import com.joshayoung.lazypizza.core.presentation.components.PizzaBottomBar
import com.joshayoung.lazypizza.core.presentation.components.SmallPizzaScaffold
import com.joshayoung.lazypizza.core.presentation.models.BottomNavItem
import com.joshayoung.lazypizza.core.presentation.utils.addOnsForPreview
import com.joshayoung.lazypizza.core.presentation.utils.previewBottomNavItems
import com.joshayoung.lazypizza.core.presentation.utils.productUiListForPreview
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.core.ui.theme.surfaceHigher
import com.joshayoung.lazypizza.core.ui.theme.surfaceHighest
import com.joshayoung.lazypizza.core.ui.theme.textPrimary
import com.joshayoung.lazypizza.core.utils.DeviceConfiguration
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@Composable
fun CartScreenRoot(
    viewModel: CartViewModel = koinViewModel(),
    bottomNavItems: List<BottomNavItem>,
    backToMenu: () -> Unit
) {
    CartScreen(
        bottomNavItems = bottomNavItems,
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onAction = { action ->
            viewModel.onAction(action)
        },
        backToMenu = backToMenu
    )
}

@Composable
fun CartScreen(
    bottomNavItems: List<BottomNavItem>,
    state: CartState,
    onAction: (CartAction) -> Unit,
    backToMenu: () -> Unit
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
                        cartItems = state.cartItems,
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
                            .background(MaterialTheme.colorScheme.background)
                            .padding(innerPadding)
                            .padding(14.dp)
                ) {
                    if (state.isLoadingCart) {
                        LoadingBox()
                    } else if (state.items.count() < 1) {
                        EmptyCart(backToMenu = backToMenu)
                    } else {
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            CartItems(state = state, onAction = onAction)
                            Column {
                                RecommendedAddOns(state.recommendedAddOns, onAction = onAction)
                                CheckOutButton(
                                    state = state,
                                    modifier =
                                        Modifier
                                            .padding(top = 20.dp)
                                )
                            }
                        }
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
                    if (state.isLoadingCart) {
                        LoadingBox()
                    } else if (state.items.count() < 1) {
                        EmptyCart(backToMenu = backToMenu)
                    } else {
                        Row(
                            modifier =
                                Modifier
                                    .padding(20.dp)
                                    .fillMaxSize()
                        ) {
                            CartItems(
                                state = state,
                                onAction = onAction,
                                modifier =
                                    Modifier
                                        .padding(end = 20.dp)
                                        .weight(1f)
                            )

                            Column(
                                modifier =
                                    Modifier
                                        .clip(shape = RoundedCornerShape(10.dp))
                                        .background(MaterialTheme.colorScheme.surfaceHigher)
                                        .padding(20.dp)
                                        .weight(1f)
                            ) {
                                RecommendedAddOns(state.recommendedAddOns, onAction = onAction)
                                CheckOutButton(
                                    state = state,
                                    modifier =
                                        Modifier
                                            .padding(top = 20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItems(
    state: CartState,
    onAction: (CartAction) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier =
            modifier
                .padding(bottom = 10.dp),
        columns = GridCells.Fixed(1)
    ) {
        items(state.items) { productUi ->
            CartItem(
                productUi,
                modifier =
                    Modifier
                        .padding(bottom = 10.dp)
                        .height(140.dp),
                onAction = onAction
            )
        }
    }
}

@Composable
fun EmptyCart(backToMenu: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            Modifier
                .padding(top = 140.dp)
    ) {
        Text("Your Cart Is Empty", style = MaterialTheme.typography.titleLarge)
        Text("Head back to the menu and grab a pizza you love.")
        Button(onClick = {
            backToMenu()
        }) {
            Text("Back to Menu")
        }
    }
}

@Composable
fun LoadingBox() {
    Box(
        modifier =
            Modifier
                .padding(top = 40.dp)
                .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
        )
    }
}

@Composable
fun CheckOutButton(
    modifier: Modifier = Modifier,
    state: CartState
) {
    Button(
        onClick = {
        },
        shape = RoundedCornerShape(20.dp),
        modifier =
            modifier
                .fillMaxWidth()
                .dropShadow(
                    shape =
                        RoundedCornerShape(20.dp),
                    shadow =
                        Shadow(
                            radius = 6.dp,
                            spread = 1.dp,
                            color =
                                MaterialTheme.colorScheme.primary.copy(
                                    alpha = 0.25f
                                ),
                            offset = DpOffset(x = 0.dp, 4.dp)
                        )
                )
    ) {
        val formatted = String.format(Locale.US, "%.2f", state.checkoutPrice)
        Text("Proceed to Checkout ($$formatted)")
    }
}

@Composable
fun RecommendedAddOns(
    addOns: List<ProductUi>,
    onAction: (CartAction) -> Unit
) {
    Column {
        Text(
            "Recommended to Add to Your Order".uppercase(),
            fontSize = 14.sp,
            modifier =
                Modifier
                    .padding(bottom = 10.dp)
        )
        LazyRow(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(addOns) { productUi ->
                AddOn(
                    productUi,
                    addToCart =
                        {
                            onAction(CartAction.AddAddOnToCart(productUi))
                        },
                    modifier =
                        Modifier
                            .width(140.dp)
                            .height(220.dp)
                )
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
            bottomNavItems = previewBottomNavItems,
            state =
                CartState(
                    isLoadingCart = false,
                    items = productUiListForPreview.take(1),
                    recommendedAddOns = addOnsForPreview
                ),
            onAction = {},
            backToMenu = {}
        )
    }
}