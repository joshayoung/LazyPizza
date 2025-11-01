package com.joshayoung.lazypizza.menu.presentation.models

import com.joshayoung.lazypizza.core.presentation.models.InCartItemUi

data class MenuItemUi(
    val menuTypeUi: MenuTypeUi,
    val products: List<InCartItemUi>,
    val startingIndex: Int
)
