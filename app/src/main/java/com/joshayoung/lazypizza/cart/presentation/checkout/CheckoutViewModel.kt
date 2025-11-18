package com.joshayoung.lazypizza.cart.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.core.data.database.entity.ProductsInCartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingsInCartEntity
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.presentation.mappers.toProduct
import com.joshayoung.lazypizza.core.presentation.mappers.toProductUi
import com.joshayoung.lazypizza.core.presentation.models.InCartItemSingleUi
import com.joshayoung.lazypizza.core.presentation.models.InCartItemUi
import com.joshayoung.lazypizza.core.presentation.utils.getMenuTypeEnum
import com.joshayoung.lazypizza.menu.data.toInCartItemUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class CheckoutViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {
    private var _state = MutableStateFlow(CheckoutState())

    init {
        val currentTime = LocalTime.now()
        var availableTime = currentTime.plusMinutes(15)
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val formattedTime = availableTime.format(formatter)
        _state.update {
            it.copy(
                pickupTime = formattedTime
            )
        }

        viewModelScope.launch {
            cartRepository
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
                    val lineItem =
                        cartRepository.insertProductId(
                            ProductsInCartEntity(
                                cartId = 1,
                                productId = action.inCartItemUi.productId
                            )
                        )
                    action.inCartItemUi.toppings.forEach { topping ->
                        cartRepository.insertToppingId(
                            ToppingsInCartEntity(
                                lineItemNumber = lineItem,
                                toppingId = topping.toppingId,
                                cartId = 1
                            )
                        )
                    }
                }
            }
            is CheckoutAction.RemoveAllFromCart -> {
                viewModelScope.launch {
                    action.inCartItemUi.lineNumbers.forEach { lineNumber ->
                        if (lineNumber != null) {
                            cartRepository.removeAllFromCart(lineNumber)
                        }
                    }
                }
            }
            is CheckoutAction.RemoveItemFromCart -> {
                viewModelScope.launch {
                    val lastLineNumber = action.inCartItemUi.lineNumbers.last()
                    if (lastLineNumber != null) {
                        val item = cartRepository.getProductInCart(lastLineNumber)
                        if (item != null) {
                            cartRepository.deleteCartItem(item)
                        }
                    }
                }
            }

            CheckoutAction.PickEarliestTime -> {
                _state.update {
                    it.copy(
                        earliestTime = true,
                        scheduleTime = false
                    )
                }
            }
            CheckoutAction.PickTime -> {
                _state.update {
                    it.copy(
                        scheduleDate = true,
                        earliestTime = false
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
                        scheduleDate = false,
                        scheduleTime = true
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
                var pickupTime = ""
                if (date.compareTo(now) == 0) {
                    pickupTime = "${_state.value.hour}:${_state.value.minute}"
                } else {
                    val formattedDate = formatDate(date, "MMMM dd")
                    val minute = _state.value.minute
                    var minuteFormatted = ""
                    if (minute.toString().length == 1) {
                        minuteFormatted = "0$minute"
                    } else {
                        minuteFormatted = minute.toString()
                    }
                    pickupTime = "$formattedDate, ${_state.value.hour}:$minuteFormatted"
                }
                _state
                    .update {
                        it.copy(
                            scheduleTime = false,
                            timeScheduled = true,
                            pickupTime = pickupTime
                        )
                    }
            }

            CheckoutAction.CloseDatePicker -> {
                _state.update {
                    it.copy(
                        scheduleDate = false
                    )
                }
            }

            CheckoutAction.CloseTimePicker -> {
                _state.update {
                    it.copy(
                        scheduleTime = false
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
//                    cartRepository.placeOrder()
                    _state
                        .update {
                            it.copy(
                                orderInProgress = true
                            )
                        }
                }
            }
        }
    }

    // TODO: This is duplicated here and in the CartViewModel.
    private fun loadCart() {
        viewModelScope.launch {
            var count = 0
            val productsInCart =
                cartRepository
                    .productsInCartWithNoToppings()
                    .combine(
                        cartRepository
                            .productsInCartWithToppings()
                    ) { productWithNoToppings, productWithToppings ->
                        val groupedByProductId = productWithNoToppings.groupBy { it.productId }
                        val inCartItemUis =
                            groupedByProductId.map { (_, productList) ->
                                productList.toInCartItemUi(key = ++count)
                            }

                        val withToppings =
                            productWithToppings.map { iic ->
                                val toppings =
                                    cartRepository.getToppingForProductInCart(
                                        iic.lineItemId
                                    )
                                val toppingsForDisplay =
                                    toppings
                                        .groupBy { it.name }
                                        .mapValues { entry -> entry.value.size }
                                // could this be generic object?
                                InCartItemSingleUi(
                                    name = iic.name,
                                    description = iic.description,
                                    imageResource = iic.imageResource,
                                    toppingsForDisplay = toppingsForDisplay,
                                    toppings = toppings,
                                    lineItemId = iic.lineItemId,
                                    imageUrl = iic.imageUrl,
                                    nameWithToppingIds = iic.nameWithToppingIds,
                                    type = getMenuTypeEnum(iic.type),
                                    price = iic.price,
                                    remoteId = iic.remoteId,
                                    productId = iic.productId
                                )
                            }

                        // group by something more unique:
                        val grp = withToppings.groupBy { it.nameWithToppingIds }

                        val inCartItemsWithToppingUis =
                            grp.map { item ->
                                val lineNumbers = item.value.map { it.lineItemId }
                                InCartItemUi(
                                    key = ++count,
                                    lineNumbers = lineNumbers,
                                    name = item.value.first().name,
                                    description = item.value.first().description,
                                    imageResource = item.value.first().imageResource,
                                    toppingsForDisplay = item.value.first().toppingsForDisplay,
                                    toppings = item.value.first().toppings,
                                    imageUrl = item.value.first().imageUrl,
                                    type =
                                        getMenuTypeEnum(
                                            item.value
                                                .first()
                                                .type.name
                                        ),
                                    price = item.value.first().price,
                                    remoteId = item.value.first().remoteId,
                                    productId = item.value.first().productId,
                                    numberInCart = lineNumbers.count()
                                )
                            }

                        inCartItemUis + inCartItemsWithToppingUis
                    }.flowOn(Dispatchers.Default)

            productsInCart.collect { inCartItems ->
                _state.update {
                    it.copy(
                        items = inCartItems,
                        checkoutPrice = getTotal(inCartItems),
                        isLoadingCart = false
                    )
                }
            }
        }
    }

    // TODO: This is duplicated here and in the CartViewModel.
    private fun getTotal(inCartItems: List<InCartItemUi>): BigDecimal {
        val base =
            inCartItems.sumOf { item ->
                item.price.toDouble() * item.numberInCart
            }
        val toppingsInCart = inCartItems.map { it.toppings }.flatMap { it }
        val toppings =
            toppingsInCart.sumOf { item ->
                item.price.toDouble()
            }

        return BigDecimal(base + toppings)
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
            java.time.LocalDateTime.ofInstant(
                java.time.Instant.ofEpochMilli(instant.toEpochMilliseconds()),
                zoneId
            )

        return localDateTime.toLocalDate()
    }
}