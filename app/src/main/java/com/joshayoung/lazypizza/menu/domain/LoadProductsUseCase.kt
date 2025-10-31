package com.joshayoung.lazypizza.menu.domain

import com.joshayoung.lazypizza.core.domain.CartRepository
import com.joshayoung.lazypizza.core.domain.network.CartRemoteDataSource
import com.joshayoung.lazypizza.menu.presentation.models.MenuItemUi

class LoadProductsUseCase(
    private val cartRepository: CartRepository,
    private val cartRemoteDataSource: CartRemoteDataSource
) {
    suspend fun execute(): List<MenuItemUi> {
        return emptyList()
//        val productUiItems =
//            cartRepository.allProductsWithCartItems().map { item ->
//                item.toProductUi()
//            }
//        val entrees = productUiItems.filter { it.type == MenuType.Entree }
//        val beverages = productUiItems.filter { it.type == MenuType.Beverage }
//        val sauces = productUiItems.filter { it.type == MenuType.Sauce }
//        val desserts = productUiItems.filter { it.type == MenuType.Dessert }
//
//        val entreeStart = 0
//        val beverageStart = entrees.count() + HEADER_LENGTH
//        val saucesStart = beverageStart + beverages.count() + HEADER_LENGTH
//        val dessertStart = saucesStart + sauces.count() + HEADER_LENGTH
//
//        val orderedMenu =
//            listOf(
//                MenuItemUi(MenuType.Entree, entrees, entreeStart),
//                MenuItemUi(MenuType.Beverage, beverages, beverageStart),
//                MenuItemUi(MenuType.Sauce, sauces, saucesStart),
//                MenuItemUi(MenuType.Dessert, desserts, dessertStart)
//            )
//
//        return orderedMenu
    }
}