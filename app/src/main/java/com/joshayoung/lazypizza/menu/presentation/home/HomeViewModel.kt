package com.joshayoung.lazypizza.menu.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.core.data.database.CartDao
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.presentation.mappers.toProduct
import com.joshayoung.lazypizza.core.presentation.utils.textAsFlow
import com.joshayoung.lazypizza.menu.domain.LoadProductsUseCase
import com.joshayoung.lazypizza.menu.presentation.models.MenuItemUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val cartRepository: CartRepository,
    private val loadProductsUseCase: LoadProductsUseCase,
    private val cartDao: CartDao
) : ViewModel() {
    private var orderedMenu: List<MenuItemUi> = emptyList()
    private var _state = MutableStateFlow(HomeState())

    companion object {
        const val HEADER_LENGTH = 1
    }

    val state =
        _state
            .onStart {
                loadData()
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(1000L),
                HomeState()
            )

    init {
        viewModelScope.launch {
            cartRepository.getNumberProductsInCart(1).collectLatest { count ->
                _state.update {
                    it.copy(
                        cartItems = count
                    )
                }
            }
        }
        _state.value.search
            .textAsFlow()
            .onEach { search ->
                searchList(search)
            }.launchIn(viewModelScope)
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.AddItemToCart -> {
                viewModelScope.launch {
                    val product = action.productUi.toProduct()
                    cartRepository.addProductToCart(product)
                }
            }

            is HomeAction.RemoveItemFromCart -> {
                viewModelScope.launch {
                    cartRepository.removeProductFromCart(action.productUi.toProduct())
                }
            }

            is HomeAction.RemoveAllFromCart -> {
                viewModelScope.launch {
                    cartRepository.removeAllFromCart(action.productUi.toProduct())
                }
            }
        }
    }

    private fun searchList(search: CharSequence) {
        if (search.count() == 0) {
            _state.update {
                it.copy(
                    items = orderedMenu,
                    noItemsFound = false
                )
            }
            return
        }

        val items =
            orderedMenu.filter { (_, values) ->
                values.any { value -> value.name.contains(search, ignoreCase = true) }
            }

        if (items.count() < 1) {
            _state.update {
                it.copy(
                    items = emptyList(),
                    noItemsFound = true
                )
            }
            return
        }

        _state.update {
            it.copy(
                items = items,
                noItemsFound = false
            )
        }
    }

    private fun loadData() {
        _state.update {
            it.copy(
                isLoadingProducts = true
            )
        }
        viewModelScope.launch {
            cartRepository.updateLocalWithRemote()
            orderedMenu = loadProductsUseCase.execute()

            _state.update {
                it.copy(
                    items = orderedMenu,
                    isLoadingProducts = false
                )
            }
        }
    }
}
