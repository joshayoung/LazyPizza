package com.joshayoung.lazypizza.search.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.domain.LazyPizzaStorage
import com.joshayoung.lazypizza.core.domain.LazyPizzaRepository
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.search.data.mappers.toProduct
import kotlinx.coroutines.launch

class DetailsScreenViewModel(
    private val lazyPizzaRepository: LazyPizzaRepository,
    private val lazyPizzaStorage: LazyPizzaStorage,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var state by mutableStateOf(DetailsState())
        private set

    val pizza: Product?
        get() {
            val stringData = savedStateHandle.get<String>("pizza")

            return stringData?.toProduct()
        }

    init {
        viewModelScope.launch {
            state =
                state.copy(
                    product = pizza,
                    token = lazyPizzaStorage.getJwt()
                )
            var toppings = lazyPizzaRepository.getTableData(BuildConfig.TOPPINGS_COLLECTION_ID)
            state =
                state.copy(
                    toppings = toppings
                )
        }
    }
}