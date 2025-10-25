package com.joshayoung.lazypizza.menu.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.isDebugInspectorInfoEnabled
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.domain.network.CartRemoteDataSource
import com.joshayoung.lazypizza.core.presentation.mappers.toProductUi
import kotlinx.coroutines.launch
import java.math.BigDecimal

class DetailsScreenViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val cartRemoteDataSource: CartRemoteDataSource,
    private val cartRepository: CartRepository
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
            productId?.let { id ->
                val product = cartRepository.getProduct(id)
                val productUi = product.toProductUi()
                state =
                    state.copy(
                        productUi = productUi,
                        totalPrice = productUi.price
                    )
//                var toppings = cartRemoteDataSource.getProducts(BuildConfig.TOPPINGS_COLLECTION_ID)
//                state =
//                    state.copy(
//                        toppings = toppings.map { it.toProductUi() }
//                    )
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
        }
    }
}