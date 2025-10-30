package com.joshayoung.lazypizza.menu.presentation.models

import com.joshayoung.lazypizza.core.domain.models.InCartItem

data class MenuItemUi(
    val menuType: MenuType,
    val products: List<InCartItem>,
    val startingIndex: Int
)
