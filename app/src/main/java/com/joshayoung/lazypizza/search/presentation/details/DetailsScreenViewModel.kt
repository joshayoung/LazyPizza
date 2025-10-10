package com.joshayoung.lazypizza.search.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.domain.LazyPizzaPreference
import com.joshayoung.lazypizza.search.data.mappers.toProduct
import com.joshayoung.lazypizza.search.data.models.Product
import com.joshayoung.lazypizza.search.domain.utils.LazyPizzaDatabase
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
//        set(value)  {
//            savedStateHandle.set("pizza", value?.toJson())
//        }

    init {
//        savedStateHandle.get<String>("pizza")?.let { pizza ->
//            val p = pizza.toProduct()
//            println()
//        }
        val v = pizza
        viewModelScope.launch {
            state.copy(
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