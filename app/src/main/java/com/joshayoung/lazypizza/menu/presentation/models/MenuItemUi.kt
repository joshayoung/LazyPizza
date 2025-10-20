package com.joshayoung.lazypizza.menu.presentation.models

data class MenuItemUi(
    val menuType: MenuType,
    val products: List<ProductUi>,
    val startingIndex: Int
)
