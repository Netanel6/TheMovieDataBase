package com.netanel.tmdb.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkAccent,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = DarkTextHigh,
    onSecondary = DarkTextHigh,
    onBackground = DarkTextHigh,
    onSurface = DarkTextHigh
)

@Composable
fun TMDBTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = AppTypography,
        content = content
    )
}
