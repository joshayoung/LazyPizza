package com.joshayoung.lazypizza.cart.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.cart.utils.OrderEvent
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.domain.CartUpdater
import com.joshayoung.lazypizza.core.presentation.FirebaseAuthUiClient
import com.joshayoung.lazypizza.core.presentation.mappers.toProduct
import com.joshayoung.lazypizza.core.presentation.mappers.toProductUi
import com.joshayoung.lazypizza.core.presentation.models.InCartItemUi
import com.joshayoung.lazypizza.menu.domain.MenuRepository
import com.joshayoung.lazypizza.order.domain.OrderRepository
import com.joshayoung.lazypizza.order.domain.models.ProductWithToppings
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.math.RoundingMode
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class CheckoutViewModel(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository,
    private val menuRepository: MenuRepository,
    private val cartUpdater: CartUpdater
) : ViewModel() {
    private var _state = MutableStateFlow(CheckoutState())

    private val firebaseAuthUiClient: FirebaseAuthUiClient = FirebaseAuthUiClient()

    private val eventChannel = Channel<OrderEvent>()
    val events = eventChannel.receiveAsFlow()

    private fun timePlusFifteen(): String {
        val currentTime = LocalTime.now()
        val availableTime = currentTime.plusMinutes(15)
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val formattedTime = availableTime.format(formatter)

        return formattedTime
    }

    init {
        _state.update {
            it.copy(
                pickupTime = timePlusFifteen()
            )
        }

        viewModelScope.launch {
            menuRepository
                .sidesNotInCart()
                .distinctUntilChanged()
                .collect { items ->
                    _state.update {
                        it.copy(
                            recommendedAddOns =
                                items
                                    .map { item -> item.toProductUi() }
                                    .shuffled()
                        )
                    }
                }
        }
    }

    val state =
        _state
            .onStart {
                loadCart()
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(1000L),
                CheckoutState()
            )

    @OptIn(ExperimentalTime::class)
    fun onAction(action: CheckoutAction) {
        when (action) {
            is CheckoutAction.AddAddOnToCart -> {
                viewModelScope.launch {
                    val product = action.productUi.toProduct()
                    cartRepository.addProductToCart(product)
                }
            }
            is CheckoutAction.AddItemToCart -> {
                viewModelScope.launch {
                    cartUpdater.insertProductWithToppings(
                        cartId = 1,
                        productId = action.inCartItemUi.productId,
                        toppings = action.inCartItemUi.toppings
                    )
                }
            }
            is CheckoutAction.RemoveAllFromCart -> {
                viewModelScope.launch {
                    cartUpdater.removeAllFromCart(
                        lineNumbers =
                            action.inCartItemUi.lineNumbers
                    )
                }
            }
            is CheckoutAction.RemoveItemFromCart -> {
                viewModelScope.launch {
                    cartUpdater.removeItemFromCart(
                        lastLineNumber = action.inCartItemUi.lineNumbers.last()
                    )
                }
            }

            CheckoutAction.PickEarliestTime -> {
                _state.update {
                    it.copy(
                        earliestTimeSet = true,
                        futureDeliveryTimeSet = false,
                        futureDeliveryDateSelected = false,
                        pickupTime = timePlusFifteen()
                    )
                }
            }
            CheckoutAction.PickDateAndTime -> {
                _state.update {
                    it.copy(
                        datePickerOpen = true,
                        futureDeliveryDateSelected = true
                    )
                }
            }

            is CheckoutAction.SetDate -> {
                if (action.date == null) {
                    return
                }

                _state.update {
                    it.copy(
                        date = action.date,
                        datePickerOpen = false,
                        timePickerOpen = true
                    )
                }
            }

            is CheckoutAction.SetTime -> {
                val selectedTime = LocalTime.of(action.hour, action.minute)
                val startingTime = LocalTime.of(10, 15)
                val endingTime = LocalTime.of(21, 45)

                if (selectedTime.isBefore(startingTime) || selectedTime.isAfter(endingTime)) {
                    _state.update {
                        it.copy(
                            timeError = "Pickup available between 10:15 and 21:45"
                        )
                    }
                    return
                }

                val currentTimePlus15 = LocalTime.now().plusMinutes(15)
                val selDate = getLocalTime(_state.value.date)
                val current = LocalDate.now()
                var today = false
                if (selDate.compareTo(current) == 0) {
                    today = true
                }

                if (today && selectedTime.isBefore(currentTimePlus15)) {
                    _state.update {
                        it.copy(
                            timeError =
                                "Pickup is possible at least 15 " +
                                    "minutes from the current time"
                        )
                    }
                    return
                }

                _state
                    .update {
                        it.copy(
                            hour = action.hour,
                            minute = action.minute
                        )
                    }

                val date = getLocalTime(_state.value.date)
                val now = LocalDate.now()
                var pickupTime: String
                if (date.compareTo(now) == 0) {
                    pickupTime = "${_state.value.hour}:${_state.value.minute}"
                } else {
                    val formattedDate = formatDate(date, "MMMM dd")
                    val minute = _state.value.minute
                    val minuteFormatted: String =
                        if (minute.toString().length == 1) {
                            "0$minute"
                        } else {
                            minute.toString()
                        }
                    pickupTime = "$formattedDate, ${_state.value.hour}:$minuteFormatted"
                }
                _state
                    .update {
                        it.copy(
                            timePickerOpen = false,
                            futureDeliveryTimeSet = true,
                            pickupTime = pickupTime
                        )
                    }
            }

            CheckoutAction.CloseDatePicker -> {
                if (_state.value.futureDeliveryTimeSet) {
                    _state.update {
                        it.copy(
                            datePickerOpen = false
                        )
                    }
                    return
                }

                _state.update {
                    it.copy(
                        earliestTimeSet = true,
                        futureDeliveryTimeSet = false,
                        futureDeliveryDateSelected = false,
                        datePickerOpen = false,
                        pickupTime = timePlusFifteen()
                    )
                }
            }

            CheckoutAction.CloseTimePicker -> {
                if (_state.value.futureDeliveryTimeSet) {
                    _state.update {
                        it.copy(
                            timePickerOpen = false
                        )
                    }
                    return
                }

                _state.update {
                    it.copy(
                        earliestTimeSet = true,
                        futureDeliveryTimeSet = false,
                        futureDeliveryDateSelected = false,
                        timePickerOpen = false,
                        pickupTime = timePlusFifteen()
                    )
                }
            }

            CheckoutAction.PlaceOrder -> {
                viewModelScope.launch {
                    _state
                        .update {
                            it.copy(
                                orderInProgress = true
                            )
                        }
                    val orderNumber = System.currentTimeMillis().toString().takeLast(5)
                    val cartItems = convertToItems(_state.value.items)
                    val json = Json.encodeToString(cartItems)
                    val user = firebaseAuthUiClient.currentUser
                    val id =
                        orderRepository.placeOrder(
                            user,
                            orderNumber,
                            _state.value.pickupTime,
                            json,
                            _state.value.checkoutPrice
                                .setScale(2, RoundingMode.HALF_UP)
                                .toString(),
                            "inProgress"
                        )
                    cartRepository.clearCartForUser(user)
                    eventChannel.send(OrderEvent(orderNumber = id))
                    _state
                        .update {
                            it.copy(
                                orderInProgress = false
                            )
                        }
                }
            }
        }
    }

    private fun convertToItems(inCartItems: List<InCartItemUi>): List<ProductWithToppings> {
        return inCartItems.map { inCartItem ->
            ProductWithToppings(
                productRemoteId = inCartItem.remoteId,
                toppingRemoteIds = inCartItem.toppings.map { topping -> topping.remoteId }
            )
        }
    }

    // TODO: This is duplicated here and in the CartViewModel.
    private fun loadCart() {
        viewModelScope.launch {
            val productsInCart = cartUpdater.productsInCart()

            productsInCart.collect { inCartItems ->
                _state.update {
                    it.copy(
                        items = inCartItems,
                        checkoutPrice = cartUpdater.getTotal(inCartItems),
                        isLoadingCart = false
                    )
                }
            }
        }
    }

    fun formatDate(
        localDate: LocalDate,
        pattern: String
    ): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)

        return localDate.format(formatter)
    }

    @OptIn(ExperimentalTime::class)
    private fun getLocalTime(date: Long?): LocalDate {
        val instant = Instant.fromEpochMilliseconds(date ?: 0)

        val zoneId = ZoneId.of("UTC")
        val localDateTime =
            LocalDateTime.ofInstant(
                java.time.Instant.ofEpochMilli(instant.toEpochMilliseconds()),
                zoneId
            )

        return localDateTime.toLocalDate()
    }
}