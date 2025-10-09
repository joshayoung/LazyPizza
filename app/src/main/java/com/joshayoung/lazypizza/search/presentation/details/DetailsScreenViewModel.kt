package com.joshayoung.lazypizza.search.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.domain.LazyPizzaPreference
import com.joshayoung.lazypizza.search.domain.utils.LazyPizzaDatabase
import kotlinx.coroutines.launch

class DetailsScreenViewModel(
    private val lazyPizzaDatabase: LazyPizzaDatabase,
    private val lazyPizzaPreference: LazyPizzaPreference,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var state by mutableStateOf(DetailsState())
        private set

    init {
        savedStateHandle.get<Int>("pizza")?.let { pizza ->
            val p = pizza
            println()
        }
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