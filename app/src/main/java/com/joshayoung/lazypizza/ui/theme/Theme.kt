package com.joshayoung.lazypizza.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LazyPizzaColorScheme =
    lightColorScheme(
        primary = primary,
        onPrimary = textOnPrimary,
        background = alternativeBackground,
        outline = outline,
        outlineVariant = outline50,
        surface = surfaceHigher,
        surfaceVariant = surfaceHighest,
        onSecondary = textSecondary
    )

@Composable
fun LazyPizzaTheme(content: @Composable () -> Unit) {
    val colorScheme = LazyPizzaColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
