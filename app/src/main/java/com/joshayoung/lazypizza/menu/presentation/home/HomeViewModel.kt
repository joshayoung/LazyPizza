package com.joshayoung.lazypizza.menu.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.cart.domain.CartRepository
import com.joshayoung.lazypizza.core.presentation.mappers.toProductUi
import com.joshayoung.lazypizza.core.presentation.utils.textAsFlow
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
    private val cartRepository: CartRepository
) : ViewModel() {
    private var orderedMenu: Map<MenuType?, List<ProductUi>> = emptyMap()
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
            val menuItems = cartRepository.getTableData(BuildConfig.MENU_ITEMS_COLLECTION_ID)
            val menuItemsGrouped = menuItems.map { it.toProductUi() }.groupBy { it.type }

            val entrees =
                menuItemsGrouped
                    .flatMap {
                        it.value
                    }.filter { it.type == MenuType.Entree }
            val beverages =
                menuItemsGrouped.flatMap { it.value }.filter {
                    it.type ==
                        MenuType.Beverage
                }
            val sauces = menuItemsGrouped.flatMap { it.value }.filter { it.type == MenuType.Sauce }
            val desserts =
                menuItemsGrouped
                    .flatMap {
                        it.value
                    }.filter { it.type == MenuType.Dessert }

            val entreeStart = 0
            val beverageStart = entrees.count() + HEADER_LENGTH
            val saucesStart = beverageStart + beverages.count() + HEADER_LENGTH
            val iceCreamStart = saucesStart + sauces.count() + HEADER_LENGTH

            // TODO: It seems to be ordered correctly, but I am adding this just in case.
            orderedMenu =
                mapOf(
                    Pair(MenuType.Entree, entrees),
                    Pair(MenuType.Beverage, beverages),
                    Pair(MenuType.Sauce, sauces),
                    Pair(MenuType.Dessert, desserts)
                )

            _state.update {
                it.copy(
                    items = orderedMenu,
                    pizzaScrollPosition = entreeStart,
                    drinkScrollPosition = beverageStart,
                    sauceScrollPosition = saucesStart,
                    iceCreamScrollPosition = iceCreamStart,
                    isLoadingProducts = false
                )
            }
        }
    }
}
