package com.joshayoung.lazypizza.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LazyPizzaColorScheme =
    lightColorScheme(
        primary = primary,
        onPrimary = textOnPrimary,
        background = background,
        outline = outline,
        outlineVariant = outline50,
        onSecondary = textSecondary
    )

@Composable
fun extendedColor(light: Color): Color = light

val ColorScheme.textPrimary: Color @Composable get() =
    extendedColor(
        light = textPrimary
    )

val ColorScheme.textSecondary8: Color @Composable get() =
    extendedColor(
        light = textSecondary8
    )

val ColorScheme.surfaceHigher: Color @Composable get() =
    extendedColor(
        light = surfaceHigher
    )

val ColorScheme.surfaceHighest: Color @Composable get() =
    extendedColor(
        light = surfaceHighest
    )

val ColorScheme.primaryGradientStart: Color @Composable get() =
    extendedColor(
        light = primaryGradientStart
    )

val ColorScheme.primaryGradientEnd: Color @Composable get() =
    extendedColor(
        light = primaryGradientEnd
    )

val ColorScheme.primary8: Color @Composable get() =
    extendedColor(
        light = primary8
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
