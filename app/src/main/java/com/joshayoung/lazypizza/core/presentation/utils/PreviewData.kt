package com.joshayoung.lazypizza.core.presentation.utils

import com.joshayoung.lazypizza.R
import com.joshayoung.lazypizza.core.utils.BottomNavItem

val previewBottomNavItems =
    listOf(
        BottomNavItem(
            label = "Menu",
            selected = true,
            clickAction = {
            },
            imageResource = R.drawable.book
        ),
        BottomNavItem(
            label = "Cart",
            selected = false,
            clickAction = {
            },
            imageResource = R.drawable.cart
        ),
        BottomNavItem(
            label = "History",
            selected = false,
            clickAction = {
            },
            imageResource = R.drawable.history
        )
    )
