package com.joshayoung.lazypizza.menu.domain

import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.cart.domain.CartRepository
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.presentation.mappers.toProductUi
import com.joshayoung.lazypizza.menu.presentation.home.HomeViewModel.Companion.HEADER_LENGTH
import com.joshayoung.lazypizza.menu.presentation.models.MenuType
import com.joshayoung.lazypizza.menu.presentation.models.ProductUi

class LoadProductsUseCase(
    private val cartRepository: CartRepository
) {
    suspend fun execute(): Map<MenuType, List<ProductUi>> {
        val menuItems =
            cartRepository.getTableData(BuildConfig.MENU_ITEMS_COLLECTION_ID).map {
                it.toProductUi()
            }
        val entrees = menuItems.filter { it.type == MenuType.Entree }
        val beverages = menuItems.filter { it.type == MenuType.Beverage }
        val sauces = menuItems.filter { it.type == MenuType.Sauce }
        val desserts = menuItems.filter { it.type == MenuType.Dessert }

        val entreeStart = 0
        val beverageStart = entrees.count() + HEADER_LENGTH
        val saucesStart = beverageStart + beverages.count() + HEADER_LENGTH
        val iceCreamStart = saucesStart + sauces.count() + HEADER_LENGTH

        val orderedMenu =
            mapOf(
                Pair(MenuType.Entree, entrees),
                Pair(MenuType.Beverage, beverages),
                Pair(MenuType.Sauce, sauces),
                Pair(MenuType.Dessert, desserts)
            )

        return orderedMenu
    }
}