package com.joshayoung.lazypizza.search.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.domain.LazyPizzaPreference
import com.joshayoung.lazypizza.search.domain.utils.LazyPizzaDatabase
import kotlinx.coroutines.launch

class DetailsScreenViewModel(
    private val lazyPizzaDatabase: LazyPizzaDatabase,
    private val lazyPizzaPreference: LazyPizzaPreference
) : ViewModel() {
    var state by mutableStateOf(DetailsState())
        private set

    init {
        viewModelScope.launch {
            state =
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