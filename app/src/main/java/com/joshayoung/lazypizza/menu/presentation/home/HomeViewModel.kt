package com.joshayoung.lazypizza.menu.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.core.data.database.entity.ProductsInCartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingsInCartEntity
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.presentation.utils.textAsFlow
import com.joshayoung.lazypizza.menu.data.toInCartItemUi
import com.joshayoung.lazypizza.menu.presentation.models.MenuItemUi
import com.joshayoung.lazypizza.menu.presentation.models.MenuTypeUi
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
    private val cartRepository: CartRepository
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
                    val lineItem =
                        cartRepository.insertProductId(
                            ProductsInCartEntity(
                                cartId = 1,
                                productId = action.inCartItemUi.productId
                            )
                        )
                    if (action.inCartItemUi.toppings.any()) {
                        action.inCartItemUi.toppings.forEach { topping ->
                            cartRepository.insertToppingId(
                                ToppingsInCartEntity(
                                    lineItemNumber = lineItem,
                                    toppingId = topping.toppingId,
                                    cartId = 1
                                )
                            )
                        }
                    }
                }
            }

            is HomeAction.RemoveItemFromCart -> {
                viewModelScope.launch {
                    val lastLineNumber = action.inCartItemUi.lineNumbers.last()
                    if (lastLineNumber == null) {
                        return@launch
                    }
                    cartRepository.getProductInCart(lastLineNumber)?.let { item ->
                        cartRepository.deleteCartItem(item)
                    }
                }
            }

            is HomeAction.RemoveAllFromCart -> {
                viewModelScope.launch {
                    action.inCartItemUi.lineNumbers.forEach { lineNumber ->
                        if (lineNumber != null) {
                            cartRepository.removeAllFromCart(lineNumber)
                        }
                    }
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
            orderedMenu
                .map { menuItem ->
                    val matches =
                        menuItem.products.filter { product ->
                            product.name.contains(search, ignoreCase = true)
                        }
                    menuItem.copy(products = matches)
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
            cartRepository
                .allProductsWithCartItems()
                .collect { productUiItems ->

                    val groupedByProductId = productUiItems.groupBy { it.productId }
                    val allProducts =
                        groupedByProductId
                            .map { (_, productList) ->
                                productList.toInCartItemUi()
                            }
                    val entrees =
                        allProducts.filter {
                            it.type == MenuTypeUi.Entree
                        }
                    val beverages =
                        allProducts.filter {
                            it.type ==
                                MenuTypeUi.Beverage
                        }
                    val sauces = allProducts.filter { it.type == MenuTypeUi.Sauce }
                    val desserts =
                        allProducts.filter {
                            it.type == MenuTypeUi.Dessert
                        }

                    val entreeStart = 0
                    val beverageStart = entrees.count() + HEADER_LENGTH
                    val saucesStart = beverageStart + beverages.count() + HEADER_LENGTH
                    val dessertStart = saucesStart + sauces.count() + HEADER_LENGTH
                    orderedMenu =
                        listOf(
                            MenuItemUi(MenuTypeUi.Entree, entrees, entreeStart),
                            MenuItemUi(MenuTypeUi.Beverage, beverages, beverageStart),
                            MenuItemUi(MenuTypeUi.Sauce, sauces, saucesStart),
                            MenuItemUi(MenuTypeUi.Dessert, desserts, dessertStart)
                        )
                    _state.update {
                        it.copy(
                            items = orderedMenu,
                            isLoadingProducts = false
                        )
                    }
                }
        }
    }
}
