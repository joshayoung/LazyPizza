package com.joshayoung.lazypizza.menu.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.core.data.database.entity.ToppingsInCartEntity
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.presentation.mappers.toProduct
import com.joshayoung.lazypizza.core.presentation.mappers.toProductUi
import com.joshayoung.lazypizza.core.presentation.mappers.toToppingUi
import com.joshayoung.lazypizza.menu.domain.MenuRepository
import com.joshayoung.lazypizza.menu.presentation.models.ToppingUi
import kotlinx.coroutines.launch
import java.math.BigDecimal

class DetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val cartRepository: CartRepository,
    private val menuRepository: MenuRepository
) : ViewModel() {
    var state by mutableStateOf(DetailsState())
        private set

    private val toppings: MutableList<ToppingUi> = mutableListOf()

    val productId: String?
        get() {
            val stringData = savedStateHandle.get<String>("productId")

            return stringData
        }

    init {
        viewModelScope.launch {
            cartRepository.updateLocalToppingsWithRemote()
            productId?.let { id ->
                val product = menuRepository.getProduct(id)
                val productUi = product.toProductUi()
                state =
                    state.copy(
                        productUi = productUi,
                        totalPrice = productUi.price
                    )
                var toppings = menuRepository.getToppings()
                state =
                    state.copy(
                        toppings = toppings.map { it.toToppingUi() }
                    )
            }
        }
    }

    fun onAction(action: DetailAction) {
        when (action) {
            is DetailAction.DecrementPrice -> {
                val newTotal = state.totalPrice - action.price
                if (newTotal < BigDecimal(0)) {
                    state =
                        state.copy(
                            totalPrice = BigDecimal(0)
                        )
                    return
                }
                state =
                    state.copy(
                        totalPrice = newTotal
                    )
            }
            is DetailAction.IncrementPrice -> {
                val newTotal = state.totalPrice + action.price
                state =
                    state.copy(
                        totalPrice = newTotal
                    )
            }

            is DetailAction.AddItemToCart -> {
                viewModelScope.launch {
                    val product = action.productUi?.toProduct()
                    if (product != null) {
                        val lineItemNumber = cartRepository.addProductToCart(product)
                        if (lineItemNumber == null) {
                            return@launch
                        }
                        toppings.forEach { topping ->
                            topping.localId?.let { id ->
                                cartRepository.insertToppingId(
                                    lineItemNumber = lineItemNumber,
                                    toppingId = id,
                                    cartId = 1
                                )
                            }
                        }
                    }
                }
            }

            is DetailAction.AddToppingToList -> toppings.add(action.toppingUi)
            is DetailAction.RemoveToppingFromList -> toppings.remove(action.toppingUi)
        }
    }
}