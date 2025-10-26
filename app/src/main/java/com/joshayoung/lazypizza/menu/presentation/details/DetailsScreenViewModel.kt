package com.joshayoung.lazypizza.menu.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.isDebugInspectorInfoEnabled
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.data.database.CartDao
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.domain.models.ProductToppings
import com.joshayoung.lazypizza.core.domain.network.CartRemoteDataSource
import com.joshayoung.lazypizza.core.presentation.mappers.toProduct
import com.joshayoung.lazypizza.core.presentation.mappers.toProductUi
import com.joshayoung.lazypizza.core.presentation.mappers.toToppingUi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.math.BigDecimal

class DetailsScreenViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val cartRemoteDataSource: CartRemoteDataSource,
    private val cartRepository: CartRepository,
    private val cartDao: CartDao
) : ViewModel() {
    var state by mutableStateOf(DetailsState())
        private set

    val productId: String?
        get() {
            val stringData = savedStateHandle.get<String>("productId")

            return stringData
        }

    init {
        viewModelScope.launch {
            cartRepository.updateLocalToppingsWithRemote()
            productId?.let { id ->
                val product = cartRepository.getProduct(id)
                val productUi = product.toProductUi()
                state =
                    state.copy(
                        productUi = productUi,
                        totalPrice = productUi.price
                    )
                // TODO: Does this need to be a flow?
                var toppings = cartRepository.getToppings().first()
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
                        cartRepository.addProductToCart(product)
                    }
                }
            }

            is DetailAction.AddTopping -> {
                viewModelScope.launch {
                    val productId = state.productUi?.localId
                    val toppingId = action.toppingUi.localId

                    // TODo: If not in cart, do not allow??
                    if (productId != null && toppingId != null) {
                        cartDao.insertToppingId(
                            ProductToppings(
                                productId,
                                toppingId,
                                cartId = 1
                            )
                        )
                    }
                }
            }
        }
    }
}