package com.joshayoung.lazypizza.cart.presentation.cart_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.domain.CartUpdater
import com.joshayoung.lazypizza.core.presentation.mappers.toProduct
import com.joshayoung.lazypizza.core.presentation.mappers.toProductUi
import com.joshayoung.lazypizza.core.presentation.models.InCartItemUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal

class CartViewModel(
    private var cartRepository: CartRepository,
    private var cartUpdater: CartUpdater
) : ViewModel() {
    private var _state = MutableStateFlow(CartState())

    val state =
        _state
            .onStart {
                loadCart()
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(1000L),
                CartState()
            )

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

    // TODO: Move logic to use case:
    private fun loadCart() {
        viewModelScope.launch {
            val productsInCart = cartUpdater.productsInCart()
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

    fun onAction(action: CartAction) {
        when (action) {
            is CartAction.AddItemToCart -> {
                viewModelScope.launch {
                    cartUpdater.insertProductWithToppings(
                        cartId = 1,
                        productId = action.inCartItemUi.productId,
                        toppings = action.inCartItemUi.toppings
                    )
                }
            }

            is CartAction.RemoveItemFromCart -> {
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

            is CartAction.RemoveAllFromCart -> {
                viewModelScope.launch {
                    action.inCartItemUi.lineNumbers.forEach { lineNumber ->
                        if (lineNumber != null) {
                            cartRepository.removeAllFromCart(lineNumber)
                        }
                    }
                }
            }

            is CartAction.AddAddOnToCart -> {
                viewModelScope.launch {
                    val product = action.productUi.toProduct()
                    cartRepository.addProductToCart(product)
                }
            }
        }
    }
}