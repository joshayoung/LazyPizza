package com.joshayoung.lazypizza.cart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.core.data.database.CartDao
import com.joshayoung.lazypizza.core.data.database.entity.ProductsInCart
import com.joshayoung.lazypizza.core.data.database.entity.ToppingsInCart
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.domain.models.InCartItem
import com.joshayoung.lazypizza.core.presentation.mappers.toProduct
import com.joshayoung.lazypizza.core.presentation.mappers.toProductUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal

class CartViewModel(
    private var cartRepository: CartRepository,
    private var cartDao: CartDao
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
            cartDao
                .sidesNotInCart()
                .collect { items ->
                    _state.update {
                        it.copy(
                            recommendedAddOns =
                                items
                                    .map { it.toProduct() }
                                    .map { it.toProductUi() }
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
            val both =
                cartDao
                    .productsInCartWithNoToppings()
                    .combine(
                        cartDao
                            .productsInCartWithToppings()
                    ) { productWithNoToppings, productWithToppings ->
                        val groupedByProductId = productWithNoToppings.groupBy { it.productId }
                        val inCartItems =
                            groupedByProductId.map { (_, productList) ->
                                InCartItem(
                                    lineNumbers = productList.map { it.lineItemId },
                                    name = productList.first().name,
                                    description = productList.first().description,
                                    imageResource = productList.first().imageResource,
                                    toppingsForDisplay = mapOf(),
                                    imageUrl = productList.first().imageUrl,
                                    type = productList.first().type ?: "",
                                    remoteId = productList.first().remoteId,
                                    price = productList.first().price,
                                    productId = productList.first().productId,
                                    numberInCart = productList.count()
                                )
                            }

                        val groupedByToppingList =
                            productWithToppings
                                .groupBy { it.name }
                        val inCartItemsTwo =
                            groupedByToppingList.map { (id, productList) ->
                                productList
                                    .map {
                                        cartDao.getToppingForProductInCart(it.lineItemId)
                                    }.flatMap { it }
                                val toppings =
                                    productList
                                        .map {
                                            cartDao.getToppingForProductInCart(it.lineItemId)
                                        }.flatMap { it }
                                val toppingsForDisplay =
                                    toppings
                                        .groupBy { it.name }
                                        .mapValues { entry -> entry.value.size }
                                InCartItem(
                                    lineNumbers = productList.map { it.lineItemId },
                                    toppings = toppings,
                                    toppingsForDisplay = toppingsForDisplay,
                                    name = productList.first().name,
                                    productId = productList.first().productId,
                                    description = productList.first().description,
                                    imageResource = productList.first().imageResource,
                                    imageUrl = productList.first().imageUrl,
                                    type = productList.first().type ?: "",
                                    remoteId = productList.first().remoteId,
                                    price = productList.first().price,
                                    numberInCart = productList.count()
                                )
                            }
                        inCartItems + inCartItemsTwo
                    }

            both.collect { inCartItems ->
                _state.update {
                    it.copy(
                        items = inCartItems,
                        checkoutPrice =
                            BigDecimal(
                                inCartItems.sumOf { lt ->
                                    lt.price.toDouble()
                                }
                            ),
                        isLoadingCart = false
                    )
                }
            }
        }
    }

    fun onAction(action: CartAction) {
        when (action) {
            is CartAction.AddItemToCart -> {
                viewModelScope.launch {
                    val lineItem =
                        cartDao.insertProductId(
                            ProductsInCart(
                                cartId = 1,
                                productId = action.inCartItem.productId
                            )
                        )
                    if (action.inCartItem.toppings.any()) {
                        action.inCartItem.toppings.forEach { topping ->
                            cartDao.insertToppingId(
                                ToppingsInCart(
                                    lineItemNumber = lineItem,
                                    toppingId = topping.toppingId,
                                    cartId = 1
                                )
                            )
                        }
                    }
                }
            }

            is CartAction.RemoveItemFromCart -> {
                viewModelScope.launch {
                    val firstLineItemNumber = action.inCartItem.lineNumbers.first()
                    if (firstLineItemNumber != null) {
                        val item = cartDao.getProductInCart(firstLineItemNumber)
                        if (item != null) {
                            cartDao.deleteCartItem(item)
                        }
                    }
                }
            }

            is CartAction.RemoveAllFromCart -> {
                viewModelScope.launch {
                    action.inCartItem.lineNumbers.forEach { lineNumber ->
                        if (lineNumber != null) {
                            cartRepository.removeAllFromCart(lineNumber)
                        }
                    }
                }
            }

            // TODO: The adds on are added a different way:
            is CartAction.AddAddOnToCart -> {
                viewModelScope.launch {
                    val product = action.productUi.toProduct()
                    cartRepository.addProductToCart(product)
                }
            }
        }
    }
}