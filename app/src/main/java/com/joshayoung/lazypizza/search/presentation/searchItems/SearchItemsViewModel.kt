package com.joshayoung.lazypizza.search.presentation.searchItems

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.core.domain.LazyPizzaPreference
import com.joshayoung.lazypizza.core.presentation.utils.textAsFlow
import com.joshayoung.lazypizza.search.domain.utils.LazyPizzaDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchItemsViewModel(
    private val lazyPizzaDatabase: LazyPizzaDatabase,
    private val lazyPizzaPreference: LazyPizzaPreference
) : ViewModel() {
    private var _state = MutableStateFlow(SearchItemsState())

    val state =
        _state
            .onStart {
                loadData()
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(1000L),
                SearchItemsState()
            )

    init {
        _state.update {
            it.copy(
                token = lazyPizzaPreference.getJwt()
            )
        }
        _state.value.search
            .textAsFlow()
            .onEach { search ->
                searchList(search)
            }.launchIn(viewModelScope)
    }

    private fun searchList(search: CharSequence) {
        // TODO: Finish this.
//        _state.update {
//            it.copy(
//            )
//        }
    }

    private fun loadData() {
        viewModelScope.launch {
            val all2 = lazyPizzaDatabase.getAllData()
            _state.update {
                it.copy(products = all2)
            }
        }
    }
}
