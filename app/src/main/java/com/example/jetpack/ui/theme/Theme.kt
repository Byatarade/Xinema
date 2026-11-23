package com.example.jetpack.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val NetflixDarkColorScheme = darkColorScheme(
    primary = NetflixRed,
    onPrimary = Color.White,
    secondary = NetflixRed,
    onSecondary = Color.White,
    background = DarkBackground,
    onBackground = LightText,
    surface = DarkSurface,
    onSurface = LightText,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = MutedText,
    secondaryContainer = DarkPill,
    onSecondaryContainer = LightText,
    outline = DividerDark
)

private val NetflixLightColorScheme = lightColorScheme(
    primary = NetflixRed,
    onPrimary = Color.White,
    secondary = NetflixRed,
    onSecondary = Color.White,
    background = LightBackground,
    onBackground = DarkText,
    surface = LightSurface,
    onSurface = DarkText,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightMutedText,
    secondaryContainer = LightPill,
    onSecondaryContainer = DarkText,
    outline = DividerLight
)

@Composable
fun JetpackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) NetflixDarkColorScheme else NetflixLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
