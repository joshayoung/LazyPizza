package com.joshayoung.lazypizza.menu.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.core.data.database.entity.ProductsInCartEntity
import com.joshayoung.lazypizza.core.data.database.entity.ToppingsInCartEntity
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.presentation.models.InCartItem
import com.joshayoung.lazypizza.core.presentation.utils.textAsFlow
import com.joshayoung.lazypizza.menu.presentation.models.MenuItemUi
import com.joshayoung.lazypizza.menu.presentation.models.MenuType
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
                                productId = action.inCartItem.productId
                            )
                        )
                    if (action.inCartItem.toppings.any()) {
                        action.inCartItem.toppings.forEach { topping ->
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
                    val lastLineNumber = action.inCartItem.lineNumbers.last()
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
                    action.inCartItem.lineNumbers.forEach { lineNumber ->
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
                                val lineNumbers = productList.mapNotNull { it.lineItemId }
                                InCartItem(
                                    lineNumbers = lineNumbers,
                                    name = productList.first().name,
                                    description = productList.first().description,
                                    imageResource = productList.first().imageResource,
                                    toppingsForDisplay = mapOf(),
                                    imageUrl = productList.first().imageUrl,
                                    type = productList.first().type,
                                    price = productList.first().price,
                                    remoteId = productList.first().remoteId,
                                    productId = productList.first().productId,
                                    numberInCart = lineNumbers.count()
                                )
                            }
                    val entrees = allProducts.filter { it.type == MenuType.Entree.name.lowercase() }
                    val beverages =
                        allProducts.filter {
                            it.type ==
                                MenuType.Beverage.name.lowercase()
                        }
                    val sauces = allProducts.filter { it.type == MenuType.Sauce.name.lowercase() }
                    val desserts =
                        allProducts.filter {
                            it.type == MenuType.Dessert.name.lowercase()
                        }

                    val entreeStart = 0
                    val beverageStart = entrees.count() + HEADER_LENGTH
                    val saucesStart = beverageStart + beverages.count() + HEADER_LENGTH
                    val dessertStart = saucesStart + sauces.count() + HEADER_LENGTH
                    orderedMenu =
                        listOf(
                            MenuItemUi(MenuType.Entree, entrees, entreeStart),
                            MenuItemUi(MenuType.Beverage, beverages, beverageStart),
                            MenuItemUi(MenuType.Sauce, sauces, saucesStart),
                            MenuItemUi(MenuType.Dessert, desserts, dessertStart)
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
