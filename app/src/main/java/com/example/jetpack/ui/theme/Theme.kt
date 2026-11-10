package com.example.jetpack.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NetflixDarkColorScheme = darkColorScheme(
    primary = NetflixRed,
    onPrimary = LightText,
    secondary = NetflixRed,
    onSecondary = LightText,
    background = DarkBackground,
    onBackground = LightText,
    surface = DarkSurface,
    onSurface = LightText,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = MutedText,
    outline = DividerDark
)

@Composable
fun JetpackTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NetflixDarkColorScheme,
        typography = Typography,
        content = content
    )
}
