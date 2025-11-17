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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joshayoung.lazypizza.cart.presentation.components.CartItem
import com.joshayoung.lazypizza.cart.presentation.components.RecommendedAddOns
import com.joshayoung.lazypizza.core.presentation.models.InCartItemUi
import com.joshayoung.lazypizza.core.presentation.utils.addOnsForPreview
import com.joshayoung.lazypizza.core.presentation.utils.inCartItemsForPreviewUis
import com.joshayoung.lazypizza.core.ui.theme.BackIcon
import com.joshayoung.lazypizza.core.ui.theme.DownIcon
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.core.ui.theme.UpIcon
import com.joshayoung.lazypizza.core.ui.theme.surfaceHigher
import com.joshayoung.lazypizza.core.ui.theme.surfaceHighest
import com.joshayoung.lazypizza.core.ui.theme.textPrimary
import com.joshayoung.lazypizza.core.utils.DeviceConfiguration
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Calendar

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
    val pagePadding = 10.dp
    val verticalPadding = 10.dp
    val datePickerState =
        rememberDatePickerState(
            selectableDates = FutureDates
        )

    if (state.scheduleDate) {
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

    if (state.scheduleTime) {
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
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier =
                    Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .background(MaterialTheme.colorScheme.outline)
                        .padding(20.dp)
            ) {
                Text("Select Time".uppercase())
                TimeInput(
                    modifier =
                        Modifier
                            .padding(0.dp),
                    state = timePickerState
                )
                Text(
                    state.timeError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
                Row {
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
            Scaffold(
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.surfaceHighest)
                        .padding(top = 60.dp)
                        .dropShadow(
                            shape =
                                RoundedCornerShape(20.dp),
                            shadow =
                                Shadow(
                                    radius = 16.dp,
                                    spread = 0.dp,
                                    color =
                                        MaterialTheme.colorScheme.textPrimary.copy(alpha = 0.04f),
                                    offset = DpOffset(x = 0.dp, -(4.dp))
                                )
                        ).clip(RoundedCornerShape(20.dp)),
                topBar = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier =
                            Modifier
                                .background(MaterialTheme.colorScheme.surfaceHigher)
                                .padding(top = 20.dp)
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth()
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .clip(CircleShape)
                                    .background(Color.LightGray.copy(alpha = 0.5f))
                                    .size(30.dp)
                        ) {
                            IconButton(onClick = {
                                backToCart()
                            }) {
                                Icon(
                                    imageVector = BackIcon,
                                    tint = MaterialTheme.colorScheme.onSecondary,
                                    contentDescription = null
                                )
                            }
                        }
                        Spacer(Modifier.weight(1f))
                        Text(
                            "Order Checkout",
                            modifier = Modifier,
                            style =
                                MaterialTheme.typography.titleSmall
                        )
                        Spacer(Modifier.weight(1f))
                    }
                }
            ) { innerPadding ->

                Column(
                    modifier =
                        Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceHigher)
                ) {
                    Box {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(1),
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .padding(pagePadding)
                        ) {
                            item {
                                TimeSelections(
                                    state = state,
                                    pickTime = {
                                        onAction(CheckoutAction.PickTime)
                                    },
                                    earliestAvailableTime = {
                                        onAction(CheckoutAction.PickEarliestTime)
                                    },
                                    modifier =
                                        Modifier
                                            .padding(vertical = verticalPadding)
                                )
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
fun TimeSelections(
    state: CheckoutState,
    pickTime: () -> Unit,
    earliestAvailableTime: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier =
            modifier
                .fillMaxWidth()
    ) {
        Text(text = "Pickup Time".uppercase())
        TimeSelection(
            onSelect = {
                earliestAvailableTime()
            },
            isSelected = state.earliestTime,
            text = "Earliest available time"
        )
        TimeSelection(
            onSelect = {
                pickTime()
            },
            isSelected = state.timeScheduled,
            text = "Schedule time"
        )
    }
}

@Composable
fun EarliestTime(
    state: CheckoutState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Earliest Pickup Time".uppercase())
        Text(state.pickupTime, style = MaterialTheme.typography.titleSmall)
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
            Text(
                "Order Total:".uppercase(),
                style = MaterialTheme.typography.bodySmall,
                modifier =
                    Modifier
                        .padding(bottom = 10.dp)
            )
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
                    recommendedAddOns = addOnsForPreview,
                    scheduleTime = true,
                    timeError = "Pickup available between 10:15 and 21:45"
                ),
            onAction = {}
        )
    }
}