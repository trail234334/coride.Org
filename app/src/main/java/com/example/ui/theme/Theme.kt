package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = VioletLight,
    onPrimary = Color.White,
    primaryContainer = DarkCard,
    onPrimaryContainer = LavenderAccent,
    secondary = LavenderAccent,
    onSecondary = DarkBackground,
    secondaryContainer = DarkSurface,
    onSecondaryContainer = LavenderAccent,
    background = DarkBackground,
    onBackground = Color.White,
    surface = DarkSurface,
    onSurface = Color.White,
    surfaceVariant = DarkCard,
    onSurfaceVariant = LavenderAccent,
    outline = BorderOutline
)

private val LightColorScheme = lightColorScheme(
    primary = VioletPrimary,
    onPrimary = Color.White,
    primaryContainer = LavenderSurface,
    onPrimaryContainer = Color(0xFF001945),
    secondary = VioletLight,
    onSecondary = Color.White,
    secondaryContainer = LavenderSurface,
    onSecondaryContainer = CharcoalText,
    background = SurfaceBase,
    onBackground = CharcoalText,
    surface = Color.White,
    onSurface = CharcoalText,
    surfaceVariant = LavenderSurface,
    onSurfaceVariant = CharcoalText,
    outline = BorderOutline
)

@Composable
fun CoRideTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
