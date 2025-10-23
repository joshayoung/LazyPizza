package com.joshayoung.lazypizza.menu.domain

import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.cart.domain.CartRepository
import com.joshayoung.lazypizza.cart.domain.network.CartRemoteDataSource
import com.joshayoung.lazypizza.core.presentation.mappers.toProductUi
import com.joshayoung.lazypizza.menu.presentation.home.HomeViewModel.Companion.HEADER_LENGTH
import com.joshayoung.lazypizza.menu.presentation.models.MenuItemUi
import com.joshayoung.lazypizza.menu.presentation.models.MenuType
import kotlinx.coroutines.flow.first

class LoadProductsUseCase(
    private val cartRepository: CartRepository,
    private val cartRemoteDataSource: CartRemoteDataSource
) {
    suspend fun execute(): List<MenuItemUi> {
        val products =
            cartRemoteDataSource.getProducts(BuildConfig.MENU_ITEMS_COLLECTION_ID)
        val cart = cartRepository.getProducts().first()
        val productUiItems =
            products.map { product ->
                val inCart = cart.any { cartItem -> (cartItem.id == product.id) }
                val numberInCart = cart.count { cartItem -> (cartItem.id == product.id) }
                product.toProductUi(inCart = inCart, numberInCart = numberInCart)
            }
        val entrees = productUiItems.filter { it.type == MenuType.Entree }
        val beverages = productUiItems.filter { it.type == MenuType.Beverage }
        val sauces = productUiItems.filter { it.type == MenuType.Sauce }
        val desserts = productUiItems.filter { it.type == MenuType.Dessert }

        val entreeStart = 0
        val beverageStart = entrees.count() + HEADER_LENGTH
        val saucesStart = beverageStart + beverages.count() + HEADER_LENGTH
        val dessertStart = saucesStart + sauces.count() + HEADER_LENGTH

        val orderedMenu =
            listOf(
                MenuItemUi(MenuType.Entree, entrees, entreeStart),
                MenuItemUi(MenuType.Beverage, beverages, beverageStart),
                MenuItemUi(MenuType.Sauce, sauces, saucesStart),
                MenuItemUi(MenuType.Dessert, desserts, dessertStart)
            )

        return orderedMenu
    }
}