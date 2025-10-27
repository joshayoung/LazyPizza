package com.joshayoung.lazypizza.cart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.presentation.mappers.toProduct
import com.joshayoung.lazypizza.core.presentation.mappers.toProductUi
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
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
        _state.update {
            it.copy(
                isLoadingCart = true
            )
        }
        cartRepository
            .productsInCart()
            .map { productsInCartList ->
                val inCartItems =
                    productsInCartList.map { productWithCartStatusEntity ->
                        productWithCartStatusEntity.toProductUi()
                    }
                _state.update {
                    it.copy(
                        items = inCartItems,
                        isLoadingCart = false
                    )
                }
                // TODO: Temporary for testing:
                _state.update {
                    it.copy(
                        recommendedAddOns = listOf(
                            ProductUi(
                                id = "2",
                                localId = 2,
                                description =
                                    "Tomato sauce, mozzarella, " +
                                            "mushrooms, olives, bell pepper, onion, corn",
                                imageResource = R.drawable.meat_lovers,
                                name = "Veggie Delight",
                                price = BigDecimal("9.79"),
                                numberInCart = 2
                            ),
                            ProductUi(
                                id = "3",
                                localId = 3,
                                description = "A delicious food",
                                imageResource = R.drawable.cookies,
                                name = "Hawaiian Pizza",
                                price = BigDecimal("10.19")
                            ),
                        )
                    )
                }
            }.launchIn(viewModelScope)
    }

    fun onAction(action: CartAction) {
        when (action) {
            is CartAction.AddItemToCart -> {
                viewModelScope.launch {
                    val product = action.productUi.toProduct()
                    cartRepository.addProductToCart(product)
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
        }
    }
}