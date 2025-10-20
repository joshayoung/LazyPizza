package com.joshayoung.lazypizza.menu.domain

import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.cart.domain.CartRepository
import com.joshayoung.lazypizza.core.presentation.mappers.toProductUi
import com.joshayoung.lazypizza.menu.presentation.home.HomeViewModel.Companion.HEADER_LENGTH
import com.joshayoung.lazypizza.menu.presentation.models.MenuItemUi
import com.joshayoung.lazypizza.menu.presentation.models.MenuType

class LoadProductsUseCase(
    private val cartRepository: CartRepository
) {
    suspend fun execute(): List<MenuItemUi> {
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