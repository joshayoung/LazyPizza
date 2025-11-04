package com.joshayoung.lazypizza.menu.presentation.details

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.core.presentation.components.PizzaAppBar
import com.joshayoung.lazypizza.core.presentation.components.ProductOrToppingImage
import com.joshayoung.lazypizza.core.presentation.components.SmallPizzaScaffold
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.core.ui.theme.surfaceHigher
import com.joshayoung.lazypizza.core.utils.DeviceConfiguration
import com.joshayoung.lazypizza.menu.presentation.components.Topping
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi
import com.joshayoung.lazypizza.menu.presentation.models.ToppingUi
import org.koin.androidx.compose.koinViewModel
import java.math.BigDecimal
import java.util.Locale
import kotlin.math.log

@Composable
fun DetailsScreenRoot(
    logOut: () -> Unit,
    isLoggedIn: Boolean,
    viewModel: DetailViewModel = koinViewModel(),
    navigateBack: () -> Unit,
    navigateToCart: () -> Unit
) {
    DetailsScreen(
        isLoggedIn = isLoggedIn,
        logOut = logOut,
        state = viewModel.state,
        navigateBack = navigateBack,
        navigateToCart = navigateToCart,
        onAction = { action ->
            viewModel.onAction(action)
        }
    )
}

@SuppressLint("DefaultLocale")
@Composable
fun DetailsScreen(
    logOut: () -> Unit,
    isLoggedIn: Boolean,
    state: DetailsState,
    navigateBack: () -> Unit,
    navigateToCart: () -> Unit,
    onAction: (DetailAction) -> Unit
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> {
            SmallPizzaScaffold(
                topAppBar = {
                    PizzaAppBar(
                        showLogo = false,
                        logOut = logOut,
                        showContact = false,
                        isAuthenticated = isLoggedIn,
                        showBackButton = true,
                        onBackClick = navigateBack
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier =
                        Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                ) {
                    DetailHeader(state = state)
                    Column(
                        modifier =
                            Modifier
                                .background(MaterialTheme.colorScheme.surfaceHigher)
                                .padding(10.dp)
                    ) {
                        DetailDescription(state)
                        Toppings(state, onAction = onAction, modifier = Modifier.weight(1f))
                        CartButton(
                            state,
                            onAction = onAction,
                            navigateBack = navigateBack,
                            navigateToCart = navigateToCart
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
            SmallPizzaScaffold(
                topAppBar = {
                    PizzaAppBar(
                        showLogo = false,
                        showContact = false,
                        showBackButton = true,
                        onBackClick = navigateBack
                    )
                }
            ) { innerPadding ->
                Row(
                    modifier =
                        Modifier
                            .padding(innerPadding)
                            .padding(10.dp)
                            .fillMaxSize()
                ) {
                    Column(
                        modifier =
                            Modifier
                                .weight(1f)
                    ) {
                        DetailHeader(state = state)
                        DetailDescription(state)
                    }
                    Column(
                        modifier =
                            Modifier
                                .background(MaterialTheme.colorScheme.surfaceHigher)
                                .weight(1f)
                                .padding(10.dp)
                    ) {
                        Toppings(
                            state,
                            onAction = onAction,
                            modifier =
                                Modifier
                                    .padding(bottom = 10.dp)
                        )
                        CartButton(
                            state,
                            onAction = onAction,
                            navigateBack = navigateBack,
                            navigateToCart = navigateToCart
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailHeader(state: DetailsState) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                ).border(
                    BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.background
                    ),
                    shape = RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp)
                ),
        contentAlignment = Alignment.Center
    ) {
        ProductOrToppingImage(
            imageResource = state.productUi?.imageResource,
            remoteImage = state.productUi?.remoteImageUrl,
            modifier =
                Modifier
                    .size(300.dp)
        )
    }
}

@Composable
fun DetailDescription(state: DetailsState) {
    Text(
        text = state.productUi?.name ?: "",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
    )
    Text(
        text = state.productUi?.description ?: "",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSecondary,
        modifier =
            Modifier
                .padding(bottom = 10.dp)
    )
}

@Composable
fun Toppings(
    state: DetailsState,
    onAction: (DetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(0.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
        ) {
            stickyHeader {
                Text(
                    "Add Extra Toppings".uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier =
                        Modifier.padding(
                            top = 8.dp,
                            bottom = 8.dp
                        )
                )
            }
            items(state.toppings) { toppingUi ->
                Topping(
                    toppingUi,
                    click = onAction,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun CartButton(
    state: DetailsState,
    onAction: (DetailAction) -> Unit,
    navigateBack: () -> Unit,
    navigateToCart: () -> Unit
) {
    Box(
        modifier =
            Modifier
                .dropShadow(
                    shape = RoundedCornerShape(20.dp),
                    shadow =
                        Shadow(
                            radius = 4.dp,
                            spread = 2.dp,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            offset = DpOffset(x = 2.dp, 2.dp)
                        )
                )
    ) {
        Button(
            onClick = {
                onAction(DetailAction.AddItemToCart(state.productUi))
                navigateToCart()
            },
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                "Add to Cart for ${String.format(Locale.US, "$%.2f", state.totalPrice)}",
                modifier =
                    Modifier
                        .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

// @Preview(
//    showBackground = true,
//    widthDp = 800,
//    heightDp = 1280
// )
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DetailsScreenPreview() {
    LazyPizzaTheme {
        DetailsScreen(
            navigateBack = {},
            state =
                DetailsState(
                    productUi =
                        ProductUi(
                            id = "3",
                            localId = 2,
                            name = "Margherita",
                            description = "Tomato sauce, Mozzarella, Fresh basic, Olive oil",
                            price = BigDecimal("1.00"),
                            imageResource = R.drawable.margherita
                        ),
                    toppings =
                        listOf(
                            ToppingUi(
                                localId = 3,
                                imageUrl = "",
                                imageResource = R.drawable.bacon,
                                name = "bacon",
                                price = BigDecimal("0.50"),
                                remoteId = ""
                            ),
                            ToppingUi(
                                localId = 3,
                                imageUrl = "",
                                imageResource = R.drawable.basil,
                                name = "basil",
                                price = BigDecimal("0.20"),
                                remoteId = ""
                            ),
                            ToppingUi(
                                localId = 3,
                                imageUrl = "",
                                imageResource = R.drawable.cheese,
                                name = "cheese",
                                price = BigDecimal("0.75"),
                                remoteId = ""
                            )
                        )
                ),
            onAction = {},
            isLoggedIn = false,
            logOut = {},
            navigateToCart = {}
        )
    }
}