package com.joshayoung.lazypizza.core.presentation.models

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItemUi(
    val label: String,
    val clickAction: () -> Unit,
    val selected: Boolean,
    val imageVector: ImageVector
)