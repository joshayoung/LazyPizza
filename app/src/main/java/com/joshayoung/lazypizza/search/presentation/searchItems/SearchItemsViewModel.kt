package com.joshayoung.lazypizza.search.presentation.searchItems

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.search.ImageResource
import com.joshayoung.lazypizza.search.domain.utils.LazyPizzaStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchItemsViewModel(
    private val lazyPizzaStorage: LazyPizzaStorage
) : ViewModel() {
    private var _state = MutableStateFlow(SearchItemsState(images = emptyList()))
    val state =
        _state
            .onStart {
                loadData()
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(1000L),
                SearchItemsState(images = emptyList())
            )

    private fun loadData() {
        viewModelScope.launch {
            val all = lazyPizzaStorage.getAllFiles()
            val i = all.map { ImageResource.RemoteFilePath(it) }
            _state.update {
                it.copy(
                    i
                )
            }
        }
    }
}
