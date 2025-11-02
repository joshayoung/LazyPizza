package com.joshayoung.lazypizza.cart.presentation.cart_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.core.data.database.entity.ProductsInCartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingsInCartEntity
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.presentation.mappers.toProduct
import com.joshayoung.lazypizza.core.presentation.mappers.toProductUi
import com.joshayoung.lazypizza.core.presentation.models.InCartItemUi
import com.joshayoung.lazypizza.menu.data.toInCartItemUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal

class CartViewModel(
    private var cartRepository: CartRepository
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
        viewModelScope.launch {
            cartRepository.getNumberProductsInCart(1).collectLatest { count ->
                _state.update {
                    it.copy(
                        cartItems = count
                    )
                }
            }
        }
    }

    private fun loadCart() {
        viewModelScope.launch {
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
                                productList.toInCartItemUi()
                            }

                        val groupedByToppingList =
                            productWithToppings
                                .groupBy { it.name }
                        val inCartItemsWithToppingUis =
                            groupedByToppingList.map { (id, productList) ->
                                productList
                                    .map {
                                        cartRepository
                                            .getToppingForProductInCart(it.lineItemId)
                                    }.flatMap { it }
                                val toppings =
                                    productList
                                        .map {
                                            cartRepository
                                                .getToppingForProductInCart(it.lineItemId)
                                        }.flatMap { it }
                                val toppingsForDisplay =
                                    toppings
                                        .groupBy { it.name }
                                        .mapValues { entry -> entry.value.size }
                                productList.toInCartItemUi(toppingsForDisplay)
                            }
                        inCartItemUis + inCartItemsWithToppingUis
                    }

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
        val total =
            inCartItems.sumOf { item ->
                item.price.toDouble() * item.numberInCart
            }

        return BigDecimal(total)
    }

    fun onAction(action: CartAction) {
        when (action) {
            is CartAction.AddItemToCart -> {
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