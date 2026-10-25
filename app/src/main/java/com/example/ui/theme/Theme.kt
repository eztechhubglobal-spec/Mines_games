package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldPrimary,
    onPrimary = EmeraldOnPrimary,
    primaryContainer = EmeraldContainer,
    onPrimaryContainer = EmeraldOnContainer,
    secondary = EmeraldSecondary,
    onSecondary = EmeraldOnPrimary,
    secondaryContainer = EmeraldSecondaryContainer,
    onSecondaryContainer = EmeraldOnSecondaryContainer,
    tertiary = GoldAccent,
    onTertiary = ObsidianBackground,
    tertiaryContainer = GoldContainer,
    onTertiaryContainer = GoldOnContainer,
    background = ObsidianBackground,
    onBackground = TextWhite,
    surface = ObsidianSurface,
    onSurface = TextWhite,
    surfaceVariant = ObsidianSurfaceVariant,
    onSurfaceVariant = TextMuted,
    outline = ObsidianStroke,
    error = DangerRed,
    onError = TextWhite,
    errorContainer = DangerContainer,
    onErrorContainer = DangerOnContainer
)

@Composable
fun MinesAiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // We enforce our crafted Dark Emerald theme to deliver a cohesive gaming HUD feel
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = ObsidianBackground.toArgb()
                window.navigationBarColor = ObsidianBackground.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
