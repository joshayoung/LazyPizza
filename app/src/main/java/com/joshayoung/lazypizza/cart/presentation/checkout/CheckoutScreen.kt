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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimeInput
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.joshayoung.lazypizza.cart.presentation.components.CartItem
import com.joshayoung.lazypizza.cart.presentation.components.RecommendedAddOns
import com.joshayoung.lazypizza.core.presentation.components.LoadingModal
import com.joshayoung.lazypizza.core.presentation.components.RoundedTopBar
import com.joshayoung.lazypizza.core.presentation.components.SmallRoundedPizzaScaffold
import com.joshayoung.lazypizza.core.presentation.models.InCartItemUi
import com.joshayoung.lazypizza.core.presentation.utils.addOnsForPreview
import com.joshayoung.lazypizza.core.presentation.utils.inCartItemsForPreviewUis
import com.joshayoung.lazypizza.core.ui.theme.DownIcon
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.core.ui.theme.UpIcon
import com.joshayoung.lazypizza.core.ui.theme.surfaceHigher
import com.joshayoung.lazypizza.core.ui.theme.surfaceHighest
import com.joshayoung.lazypizza.core.utils.DeviceConfiguration
import com.joshayoung.lazypizza.core.utils.ObserveAsEvents
import com.joshayoung.lazypizza.core.utils.Routes
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Locale

@Composable
fun CheckoutScreenRoot(
    viewModel: CheckoutViewModel = koinViewModel(),
    backToCart: () -> Unit,
    navController: NavController
) {
    ObserveAsEvents(viewModel.events) { event ->
        navController.navigate(
            Routes.Confirmation.toString() +
                "?orderNumber=${event.orderNumber}"
        )
    }

    CheckoutScreen(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onAction = { action ->
            viewModel.onAction(action)
        },
        backToCart = backToCart
    )
}

object FutureDates : SelectableDates {
    private val now = LocalDate.now()
    private val startOfDay = now.atTime(0, 0, 0, 0).toEpochSecond(ZoneOffset.UTC) * 1000

    override fun isSelectableYear(year: Int): Boolean {
        return year >= now.year
    }

    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis >= startOfDay
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    state: CheckoutState,
    onAction: (CheckoutAction) -> Unit,
    backToCart: () -> Unit
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    val isOpen = remember { mutableStateOf(false) }
    val verticalPadding = 6.dp
    val datePickerState =
        rememberDatePickerState(
            selectableDates = FutureDates
        )

    if (state.datePickerOpen) {
        DatePickerDialog(
            onDismissRequest = {},
            confirmButton = {
                Button(onClick = {
                    onAction(CheckoutAction.SetDate(datePickerState.selectedDateMillis))
                }) {
                    Text("Ok")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onAction(CheckoutAction.CloseDatePicker)
                }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                dateFormatter = DatePickerDefaults.dateFormatter(selectedDateSkeleton = "MMMM dd")
            )
        }
    }

    if (state.timePickerOpen) {
        val currentTime = Calendar.getInstance()

        val timePickerState =
            rememberTimePickerState(
                initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
                initialMinute = currentTime.get(Calendar.MINUTE),
                is24Hour = true
            )
        Dialog(
            onDismissRequest = {
            }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier =
                    Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .background(MaterialTheme.colorScheme.surfaceHighest)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 20.dp)
                ) {
                    Text(
                        "Select Time".uppercase(),
                        style = MaterialTheme.typography.titleSmall,
                        modifier =
                            Modifier
                                .align(
                                    alignment =
                                        Alignment.Start
                                ).padding(start = 20.dp)
                                .padding(bottom = 20.dp)
                    )
                    TimeInput(
                        modifier =
                            Modifier
                                .width(220.dp),
                        state = timePickerState
                    )
                    Text(
                        state.timeError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier =
                        Modifier
                            .width(220.dp)
                            .padding(vertical = 10.dp)
                ) {
                    TextButton(onClick = {
                        onAction(CheckoutAction.CloseTimePicker)
                    }, modifier = Modifier) {
                        Text("Cancel")
                    }
                    Button(onClick = {
                        onAction(
                            CheckoutAction.SetTime(timePickerState.hour, timePickerState.minute)
                        )
                    }) {
                        Text("Ok")
                    }
                }
            }
        }
    }

    when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> {
            SmallRoundedPizzaScaffold(
                topPadding = 60.dp,
                topBar = {
                    RoundedTopBar(backToCart = backToCart)
                }
            ) { innerPadding ->
                Box(
                    modifier =
                        Modifier
                            .padding(innerPadding)
                            .background(MaterialTheme.colorScheme.surfaceHigher)
                            .fillMaxHeight()
                            .padding(horizontal = 16.dp)
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        modifier =
                            Modifier
                                .padding(bottom = 100.dp)
                    ) {
                        item {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(top = 20.dp)
                                        .padding(bottom = 10.dp)
                                        .padding(vertical = verticalPadding)
                            ) {
                                Text(
                                    text = "Pickup Time".uppercase(),
                                    style =
                                        MaterialTheme.typography.bodySmall
                                )
                                TimeSelections(
                                    state = state,
                                    pickTime = {
                                        onAction(CheckoutAction.PickDateAndTime)
                                    },
                                    earliestAvailableTime = {
                                        onAction(CheckoutAction.PickEarliestTime)
                                    },
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                )
                            }
                        }
                        item {
                            EarliestTime(
                                state = state,
                                modifier =
                                    Modifier
                                        .padding(vertical = verticalPadding)
                            )
                        }
                        item {
                            CheckoutBorder(
                                modifier =
                                    Modifier
                                        .padding(vertical = 10.dp)
                                        .padding(vertical = verticalPadding)
                            )
                        }
                        item {
                            Column(
                                modifier =
                                    Modifier
                                        .fillMaxHeight()
                                        .padding(vertical = verticalPadding)
                            ) {
                                AccordionHeader(
                                    isOpen,
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                )
                            }
                        }
                        accordionItems(
                            state = state,
                            onAction = onAction,
                            isOpen = isOpen
                        )
                        item {
                            CheckoutBorder(
                                modifier =
                                    Modifier
                                        .padding(vertical = 10.dp)
                                        .padding(vertical = verticalPadding)
                            )
                        }

                        item {
                            RecommendedAddOns(
                                header = "Recommended Add-Ons",
                                modifier = Modifier.height(200.dp),
                                addOns = state.recommendedAddOns,
                                addProductToCart = {
                                    onAction(CheckoutAction.AddAddOnToCart(it))
                                }
                            )
                        }
                        item {
                            CheckoutBorder(
                                modifier =
                                    Modifier
                                        .padding(vertical = 10.dp)
                                        .padding(vertical = verticalPadding)
                            )
                        }
                        item {
                            Comments()
                        }
                    }
                    Column(
                        modifier =
                            Modifier.align(Alignment.BottomCenter)
                    ) {
                        OrderTotal(
                            state = state,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        )
                        PlaceOrderButton(onAction = onAction, modifier = Modifier.fillMaxWidth())
                    }
                }
            }
            if (state.orderInProgress) {
                LoadingModal()
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
                    RoundedTopBar(backToCart = backToCart)
                }
            ) { innerPadding ->
                Box(
                    modifier =
                        Modifier
                            .padding(innerPadding)
                            .background(MaterialTheme.colorScheme.surfaceHigher)
                            .fillMaxHeight()
                            .padding(horizontal = 20.dp)
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier =
                            Modifier
                                .padding(bottom = 100.dp)
                    ) {
                        item(span = { GridItemSpan(2) }) {
                            Column(
                                modifier =
                                    Modifier
                                        .padding(top = 20.dp)
                                        .padding(bottom = 10.dp)
                            ) {
                                Text(
                                    text = "Pickup Time".uppercase(),
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(top = 10.dp)
                                ) {
                                    TimeSelections(
                                        state = state,
                                        pickTime = {
                                            onAction(CheckoutAction.PickDateAndTime)
                                        },
                                        earliestAvailableTime = {
                                            onAction(CheckoutAction.PickEarliestTime)
                                        },
                                        modifier =
                                            Modifier
                                                .weight(1f)
                                    )
                                }
                            }
                        }
                        item(span = { GridItemSpan(2) }) {
                            CheckoutBorder(modifier = Modifier.padding(vertical = 10.dp))
                        }
                        item(span = { GridItemSpan(2) }) {
                            EarliestTime(
                                state = state,
                                modifier = Modifier
                            )
                        }
                        item(span = { GridItemSpan(2) }) {
                            CheckoutBorder(modifier = Modifier.padding(vertical = 10.dp))
                        }
                        item(span = { GridItemSpan(2) }) {
                            AccordionHeader(
                                isOpen,
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                            )
                        }
                        accordionItems(
                            state = state,
                            onAction = onAction,
                            isOpen = isOpen
                        )
                        item(span = { GridItemSpan(2) }) {
                            CheckoutBorder(modifier = Modifier.padding(vertical = 10.dp))
                        }
                        item(span = { GridItemSpan(2) }) {
                            RecommendedAddOns(
                                modifier =
                                Modifier,
                                addOns = state.recommendedAddOns,
                                addProductToCart = {
                                    onAction(CheckoutAction.AddAddOnToCart(it))
                                }
                            )
                        }
                        item(span = { GridItemSpan(2) }) {
                            CheckoutBorder(modifier = Modifier.padding(vertical = 10.dp))
                        }

                        item(span = { GridItemSpan(2) }) {
                            Comments()
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier =
                            Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                    ) {
                        OrderTotal(
                            state = state,
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        )
                        PlaceOrderButton(onAction = onAction, modifier = Modifier.weight(1f))
                    }
                }
            }
            if (state.orderInProgress) {
                LoadingModal()
            }
        }
    }
}

@Composable
fun CheckoutBorder(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .border(
                    width = 1.dp,
                    color =
                        MaterialTheme.colorScheme.outline
                ).height(1.dp)
                .fillMaxWidth()
    )
}

@Composable
fun OrderTotal(
    state: CheckoutState,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start
) {
    Row(
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            modifier
                .background(MaterialTheme.colorScheme.surfaceHigher)
    ) {
        Text(
            "Order Total:".uppercase(),
            style = MaterialTheme.typography.bodySmall
        )
        val formatted = String.format(Locale.US, "$%.2f", state.checkoutPrice)
        Text(formatted, style = MaterialTheme.typography.titleSmall)
    }
}

fun LazyGridScope.accordionItems(
    state: CheckoutState,
    onAction: (CheckoutAction) -> Unit,
    isOpen: MutableState<Boolean>
) {
    items(
        state.items,
        key = { it.key }
    ) { inCartItem ->
        Accordion(inCartItem, onAction = onAction, isOpen)
    }
}

@Composable
fun TimeSelections(
    state: CheckoutState,
    pickTime: () -> Unit,
    earliestAvailableTime: () -> Unit,
    modifier: Modifier = Modifier
) {
    TimeSelection(
        modifier = modifier,
        onSelect = {
            earliestAvailableTime()
        },
        isSelected = state.earliestAvailableActive,
        text = "Earliest available time"
    )
    TimeSelection(
        modifier = modifier,
        onSelect = {
            pickTime()
        },
        isSelected = state.customScheduleActive,
        text = "Schedule time"
    )
}

@Composable
fun EarliestTime(
    state: CheckoutState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier =
        Modifier
    ) {
        Row(
            modifier =
                modifier
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Earliest Pickup Time".uppercase(), style = MaterialTheme.typography.bodySmall)
            Text(state.pickupTime, style = MaterialTheme.typography.titleSmall)
        }
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
                .fillMaxHeight()
    ) {
        Text(text = "Order Details".uppercase(), style = MaterialTheme.typography.bodySmall)
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
                    .size(10.dp)
        ) {
            val icon = if (isOpen.value) UpIcon else DownIcon
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun Comments(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .heightIn(min = 92.dp)
                .fillMaxWidth()
    ) {
        Text(
            "Comments".uppercase(),
            style = MaterialTheme.typography.labelMedium,
            modifier =
                Modifier
                    .padding(bottom = 10.dp)
        )
        TextField(
            colors =
                TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
            placeholder = {
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
fun PlaceOrderButton(
    modifier: Modifier = Modifier,
    onAction: (CheckoutAction) -> Unit
) {
    Button(
        modifier = modifier,
        onClick = {
            onAction(CheckoutAction.PlaceOrder)
        }
    ) {
        Text(
            "Place Order",
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TimeSelection(
    text: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            modifier
                .border(
                    1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(100.dp)
                ).padding(
                    start = 10.dp,
                    end = 20.dp
                )
    ) {
        RadioButton(onClick = {
            onSelect()
        }, selected = isSelected)
        Text(text)
    }
}

@Preview(
    showBackground = true,
    widthDp = 400,
    heightDp = 1380
)
@Preview(
    showBackground = true,
    widthDp = 800,
    heightDp = 1280
)
@Composable
private fun CheckoutScreenPreview() {
    LazyPizzaTheme {
        CheckoutScreen(
            backToCart = {},
            state =
                CheckoutState(
                    items = inCartItemsForPreviewUis,
                    orderInProgress = false,
                    recommendedAddOns = addOnsForPreview,
                    timePickerOpen = false
//                    timeError = "Pickup available between 10:15 and 21:45"
                ),
            onAction = {}
        )
    }
}