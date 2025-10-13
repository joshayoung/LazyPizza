package com.joshayoung.lazypizza.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LazyPizzaColorScheme =
    lightColorScheme(
        primary = LazyPizzaColors.primary,
        onPrimary = LazyPizzaColors.textOnPrimary,
        background = LazyPizzaColors.background,
        outline = LazyPizzaColors.outline,
        outlineVariant = LazyPizzaColors.outline50,
        onSecondary = LazyPizzaColors.textSecondary
    )

@Composable
fun extendedColor(light: Color): Color = light

val ColorScheme.textPrimary: Color @Composable get() =
    extendedColor(
        light = LazyPizzaColors.textPrimary
    )

val ColorScheme.textSecondary8: Color @Composable get() =
    extendedColor(
        light = LazyPizzaColors.textSecondary8
    )

val ColorScheme.surfaceHigher: Color @Composable get() =
    extendedColor(
        light = LazyPizzaColors.surfaceHigher
    )

val ColorScheme.surfaceHighest: Color @Composable get() =
    extendedColor(
        light = LazyPizzaColors.surfaceHighest
    )

val ColorScheme.primaryGradientStart: Color @Composable get() =
    extendedColor(
        light = LazyPizzaColors.primaryGradientStart
    )

val ColorScheme.primaryGradientEnd: Color @Composable get() =
    extendedColor(
        light = LazyPizzaColors.primaryGradientEnd
    )

val ColorScheme.primary8: Color @Composable get() =
    extendedColor(
        light = LazyPizzaColors.primary8
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
