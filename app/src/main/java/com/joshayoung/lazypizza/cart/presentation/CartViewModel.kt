package com.joshayoung.lazypizza.cart.presentation

import androidx.core.os.registerForAllProfilingResults
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.core.data.database.CartDao
import com.joshayoung.lazypizza.core.data.database.entity.ProductInCartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingInCartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingsInCart
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.domain.models.CartItem
import com.joshayoung.lazypizza.core.presentation.mappers.toProduct
import com.joshayoung.lazypizza.core.presentation.mappers.toProductUi
import com.joshayoung.lazypizza.menu.presentation.models.MenuType
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import kotlin.collections.sumOf

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
        val productWithToppings = cartDao.productsInCartWithToppings()
        val productWithNoToppings = cartDao.productsInCartWithNoToppings()

        viewModelScope.launch {
            productWithToppings
                .combine(productWithNoToppings) { one, two ->
                    one + two
                }.collect { both ->
                    val cartItems =
                        both.map { cartItem ->
                            val toppings =
                                cartDao.getToppingsForProductInCart(cartItem.lineItemId ?: 0)
                            var ttt: Double = 0.0

                            toppings.forEach { tt ->
                                ttt += tt.price.toDouble() *
                                    tt.numberOfToppings *
                                    (cartItem.numberInCart?.toDouble() ?: 0.0)
                            }
                            val totalToppings = toppings.sumOf { it.price.toDouble() }
                            val totalPrice = cartItem.price?.toDouble() ?: 0.0
                            val t = totalPrice * (cartItem.numberInCart?.toDouble() ?: 0.0)
                            val total = ttt + t
                            ProductUi(
                                id = cartItem.remoteId ?: "1",
                                name = cartItem.name ?: "",
                                lineItemId = cartItem.lineItemId,
                                price = BigDecimal(cartItem.price),
                                description = cartItem.description,
                                imageUrl = cartItem.imageUrl,
                                imageResource = cartItem.imageResource,
                                numberInCart = cartItem.numberInCart ?: 0,
                                localId = cartItem.productId,
                                inCart = (cartItem.numberInCart ?: 0) > 0,
                                toppingTotal = toppings.sumOf { BigDecimal(it.price) },
                                toppings = toppings,
                                totalPrice = BigDecimal(total)
                            )
                        }
                    val addOns =
                        cartDao
                            .sidesNotInCart()
                            .map {
                                it.toProduct()
                            }.map { it.toProductUi() }
                            .shuffled()
                    _state.update {
                        it.copy(
                            items = cartItems,
                            checkoutPrice = cartItems.sumOf { lt -> lt.totalPrice },
                            recommendedAddOns = addOns,
                            isLoadingCart = false
                        )
                    }
                }
        }
    }

    fun addProductWithAddOns(productUi: ProductUi) {
        val product = productUi.toProduct()
        val toppings = productUi.toppings
        viewModelScope.launch {
            val lineItemNumber = cartRepository.addProductToCart(product)
            if (lineItemNumber == null) {
                return@launch
            }
            toppings?.forEach { topping ->
                cartDao.insertToppingId(
                    ToppingsInCart(
                        lineItemNumber = lineItemNumber,
                        toppingId = topping.toppingId,
                        cartId = 1
                    )
                )
            }
        }
    }

    fun onAction(action: CartAction) {
        when (action) {
            is CartAction.AddItemToCart -> {
                viewModelScope.launch {
                    action.productUi.toppings?.count()?.let {
                        if (it > 0) {
                            addProductWithAddOns(action.productUi)
                        } else {
                            val product = action.productUi.toProduct()
                            cartRepository.addProductToCart(product)
                        }
                    }
                }
            }

            is CartAction.RemoveItemFromCart -> {
                viewModelScope.launch {
                    cartRepository.removeProductFromCart(action.productUi.toProduct())
                }
            }

            is CartAction.RemoveAllFromCart -> {
                viewModelScope.launch {
                    cartRepository.removeAllFromCart(action.productUi.toProduct())
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