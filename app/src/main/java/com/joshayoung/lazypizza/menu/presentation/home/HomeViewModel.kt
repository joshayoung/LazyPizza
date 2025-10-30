package com.joshayoung.lazypizza.menu.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.core.data.database.CartDao
import com.joshayoung.lazypizza.core.data.database.entity.ProductWithCartStatusEntity
import com.joshayoung.lazypizza.core.data.database.entity.ProductsInCart
import com.joshayoung.lazypizza.core.data.database.entity.ToppingsInCart
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.domain.models.InCartItem
import com.joshayoung.lazypizza.core.presentation.utils.textAsFlow
import com.joshayoung.lazypizza.menu.domain.LoadProductsUseCase
import com.joshayoung.lazypizza.menu.presentation.models.MenuItemUi
import com.joshayoung.lazypizza.menu.presentation.models.MenuType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
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
//                viewModelScope.launch {
//                    val product = action.productUi.toProduct()
//                    cartRepository.addProductToCart(product)
//                }
//
                viewModelScope.launch {
                    val lineItem =
                        cartDao.insertProductId(
                            ProductsInCart(
                                cartId = 1,
                                productId = action.inCartItem.productId
                            )
                        )
                    if (action.inCartItem.toppings.any()) {
                        action.inCartItem.toppings.forEach { topping ->
                            cartDao.insertToppingId(
                                ToppingsInCart(
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
                    val firstLineItemNumber = action.inCartItem.lineNumbers.first()
                    if (firstLineItemNumber != null) {
                        val item = cartDao.getProductInCart(firstLineItemNumber)
                        if (item != null) {
                            cartDao.deleteCartItem(item)
                        }
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
            cartRepository
                .allProductsWithCartItems()
                .distinctUntilChanged()
                .collect { productUiItems ->

                    val groupedByProductId = productUiItems.groupBy { it.productId }
                    val allProducts =
                        groupedByProductId
                            .map { (_, productList) ->
                                doSomething(productList)
                            }.map { itm ->
                                InCartItem(
                                    lineNumbers = itm.lineNumbers,
                                    name = itm.name,
                                    toppingsForDisplay = mapOf(),
                                    productId = itm.productId,
                                    description = itm.description,
                                    numberInCart = itm.numberInCart,
                                    remoteId = itm.remoteId,
                                    imageResource = itm.imageResource,
                                    imageUrl = itm.imageUrl,
                                    type = itm.type,
                                    price = itm.price
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
                    val menuItems =
                        listOf(
                            MenuItemUi(MenuType.Entree, entrees, entreeStart),
                            MenuItemUi(MenuType.Beverage, beverages, beverageStart),
                            MenuItemUi(MenuType.Sauce, sauces, saucesStart),
                            MenuItemUi(MenuType.Dessert, desserts, dessertStart)
                        )
                    _state.update {
                        it.copy(
                            items = menuItems,
                            isLoadingProducts = false
                        )
                    }
                }
        }
    }

    private fun doSomething(productList: List<ProductWithCartStatusEntity>): InCartItem {
        val lineNumbers = productList.mapNotNull { it.lineItemId }
        return InCartItem(
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
}
