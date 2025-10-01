package com.joshayoung.lazypizza.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LazyPizzaColorScheme = darkColorScheme(
    primary = primary,
    onPrimary = textOnPrimary,
    background = background,
    outline = outline,
    outlineVariant = outline50,
    surface = surfaceHigher,
    surfaceVariant = surfaceHighest,
    onSecondary = textSecondary
)

@Composable
fun LazyPizzaTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LazyPizzaColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}