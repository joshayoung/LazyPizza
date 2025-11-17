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
import kotlin.collections.component1
import kotlin.collections.component2

class CheckoutViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {
    private var _state = MutableStateFlow(CheckoutState())

    init {
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
                        scheduleTime = true,
                        earliestTime = false
                    )
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
}