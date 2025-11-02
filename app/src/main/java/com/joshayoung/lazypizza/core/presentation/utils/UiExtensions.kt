package com.joshayoung.lazypizza.core.presentation.utils

import com.joshayoung.lazypizza.menu.presentation.models.MenuTypeUi

public fun getMenuTypeEnum(menuType: String?): MenuTypeUi {
    when (menuType?.lowercase()) {
        "entree" -> {
            return MenuTypeUi.Entree
        }
        "dessert" -> {
            return MenuTypeUi.Dessert
        }
        "beverage" -> {
            return MenuTypeUi.Beverage
        }
        "sauce" -> {
            return MenuTypeUi.Sauce
        }
        else -> {
            return MenuTypeUi.Unknown
        }
    }
}
