package com.joshayoung.lazypizza.core.presentation.models

data class BottomNavItemUi(
    val label: String,
    val clickAction: () -> Unit,
    val selected: Boolean,
    val imageResource: Int
)