package com.joshayoung.lazypizza.menu.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.cart.domain.CartRepository
import com.joshayoung.lazypizza.core.presentation.mappers.toProductUi
import com.joshayoung.lazypizza.core.presentation.utils.textAsFlow
import com.joshayoung.lazypizza.menu.domain.LoadProductsUseCase
import com.joshayoung.lazypizza.menu.presentation.models.MenuType
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val cartRepository: CartRepository,
    private val loadProductsUseCase: LoadProductsUseCase
) : ViewModel() {
    private var orderedMenu: Map<MenuType, List<ProductUi>> = emptyMap()
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
            cartRepository.getCartData().collect { data ->
                val count = (data ?: "0").toInt()
                _state.update {
                    it.copy(
                        cartItems = count
                    )
                }
                println()
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
                    cartRepository.addToCart(1)
                }
            }

            is HomeAction.RemoveItemFromCart -> {
                viewModelScope.launch {
                    cartRepository.removeFromCart()
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
//

        if (items.count() < 1) {
            _state.update {
                it.copy(
                    items = emptyMap(),
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
            orderedMenu = loadProductsUseCase.execute()

            _state.update {
                it.copy(
                    items = orderedMenu,
                    pizzaScrollPosition = 0, // entreeStart,
                    drinkScrollPosition = 0, // beverageStart,
                    sauceScrollPosition = 0, // saucesStart,
                    iceCreamScrollPosition = 0, // iceCreamStart,
                    isLoadingProducts = false
                )
            }
        }
    }
}
