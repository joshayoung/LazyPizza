package com.joshayoung.lazypizza.menu.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.domain.AuthRepository
import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.domain.CartUpdater
import com.joshayoung.lazypizza.core.presentation.utils.textAsFlow
import com.joshayoung.lazypizza.menu.data.mappers.toInCartItemUi
import com.joshayoung.lazypizza.menu.presentation.models.MenuItemUi
import com.joshayoung.lazypizza.menu.presentation.models.MenuTypeUi
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
    private val authRepository: AuthRepository,
    private val cartUpdater: CartUpdater
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
                    cartUpdater.insertProductWithToppings(
                        cartId = 1,
                        productId = action.inCartItemUi.productId,
                        toppings = action.inCartItemUi.toppings
                    )
                }
            }

            is HomeAction.RemoveItemFromCart -> {
                viewModelScope.launch {
                    cartUpdater.removeItemFromCart(
                        lastLineNumber = action.inCartItemUi.lineNumbers.last()
                    )
                }
            }

            is HomeAction.RemoveAllFromCart -> {
                viewModelScope.launch {
                    cartUpdater.removeAllFromCart(
                        lineNumbers =
                            action.inCartItemUi.lineNumbers
                    )
                }
            }

            HomeAction.SignOut -> {
                viewModelScope.launch {
                    logOutTransferAndClearCart()
                }
            }
        }
    }

    private suspend fun logOutTransferAndClearCart() {
        authRepository.logoutWithFirebase()
        val user = FirebaseAuth.getInstance().uid
        cartRepository.clearCartForUser(user)
        cartRepository.transferCartTo(
            user,
            BuildConfig.GUEST_USER
        )
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
