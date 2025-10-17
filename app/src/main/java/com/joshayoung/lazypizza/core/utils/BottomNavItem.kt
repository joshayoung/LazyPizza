package com.joshayoung.lazypizza.core.utils

data class BottomNavItem(
    val label: String,
    val clickAction: () -> Unit,
    val selected: Boolean,
    val imageResource: Int
)
