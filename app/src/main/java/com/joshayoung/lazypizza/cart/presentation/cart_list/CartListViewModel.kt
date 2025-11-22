package com.joshayoung.lazypizza.cart.presentation.cart_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.domain.CartUpdater
import com.joshayoung.lazypizza.core.presentation.mappers.toProduct
import com.joshayoung.lazypizza.core.presentation.mappers.toProductUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartListViewModel(
    private var cartRepository: CartRepository,
    private var cartUpdater: CartUpdater
) : ViewModel() {
    private var _state = MutableStateFlow(CartListState())

    val state =
        _state
            .onStart {
                loadCart()
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(1000L),
                CartListState()
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

    fun onAction(action: CartListAction) {
        when (action) {
            is CartListAction.AddItemToCartList -> {
                viewModelScope.launch {
                    cartUpdater.insertProductWithToppings(
                        cartId = 1,
                        productId = action.inCartItemUi.productId,
                        toppings = action.inCartItemUi.toppings
                    )
                }
            }

            is CartListAction.RemoveItemFromCartList -> {
                viewModelScope.launch {
                    cartUpdater.removeItemFromCart(
                        lastLineNumber = action.inCartItemUi.lineNumbers.last()
                    )
                }
            }

            is CartListAction.RemoveAllFromCartList -> {
                viewModelScope.launch {
                    cartUpdater.removeAllFromCart(
                        lineNumbers =
                            action.inCartItemUi.lineNumbers
                    )
                }
            }

            is CartListAction.AddAddOnToCartList -> {
                viewModelScope.launch {
                    val product = action.productUi.toProduct()
                    cartRepository.addProductToCart(product)
                }
            }
        }
    }
}