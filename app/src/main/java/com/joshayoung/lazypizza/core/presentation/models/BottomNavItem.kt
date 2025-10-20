package com.joshayoung.lazypizza.core.presentation.models

data class BottomNavItem(
    val label: String,
    val clickAction: () -> Unit,
    val selected: Boolean,
    val imageResource: Int
)