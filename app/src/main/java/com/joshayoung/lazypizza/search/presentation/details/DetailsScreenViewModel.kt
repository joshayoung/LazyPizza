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
                    productUi = pizza
                )
            var toppings = lazyPizzaRepository.getTableData(BuildConfig.TOPPINGS_COLLECTION_ID)
            state =
                state.copy(
                    toppings = toppings.map { it.toProductUi() }
                )
        }
    }
}