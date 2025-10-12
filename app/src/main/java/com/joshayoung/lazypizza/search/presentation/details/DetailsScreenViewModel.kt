package com.joshayoung.lazypizza.search.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.domain.LazyPizzaRepository
import com.joshayoung.lazypizza.core.toProductUi
import com.joshayoung.lazypizza.search.data.mappers.toProduct
import com.joshayoung.lazypizza.search.presentation.models.ProductUi
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

class DetailsScreenViewModel(
    private val lazyPizzaRepository: LazyPizzaRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var state by mutableStateOf(DetailsState())
        private set

    val pizza: ProductUi?
        get() {
            val stringData = savedStateHandle.get<String>("pizza")

            return stringData?.toProduct()
        }

    init {
        viewModelScope.launch {
            state =
                state.copy(
                    productUi = pizza,
                    totalPrice = BigDecimal(pizza?.price).setScale(2, RoundingMode.HALF_UP)
                )
            var toppings = lazyPizzaRepository.getTableData(BuildConfig.TOPPINGS_COLLECTION_ID)
            state =
                state.copy(
                    toppings = toppings.map { it.toProductUi() }
                )
        }
    }

    fun onAction(action: DetailAction) {
        when (action) {
            is DetailAction.DecrementPrice -> {
                val newTotal = state.totalPrice - BigDecimal(action.price)
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
                val newTotal = state.totalPrice + BigDecimal(action.price)
                state =
                    state.copy(
                        totalPrice = newTotal
                    )
            }
        }
    }
}