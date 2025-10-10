package com.joshayoung.lazypizza.search.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.domain.LazyPizzaDatabase
import com.joshayoung.lazypizza.core.domain.LazyPizzaPreference
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.search.data.mappers.toProduct
import kotlinx.coroutines.launch

class DetailsScreenViewModel(
    private val lazyPizzaDatabase: LazyPizzaDatabase,
    private val lazyPizzaPreference: LazyPizzaPreference,
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
                    token = lazyPizzaPreference.getJwt()
                )
            var toppings = lazyPizzaDatabase.getTableData(BuildConfig.TOPPINGS_COLLECTION_ID)
            state =
                state.copy(
                    toppings = toppings
                )
        }
    }
}