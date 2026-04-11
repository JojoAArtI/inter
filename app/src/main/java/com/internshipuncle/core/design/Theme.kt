package com.internshipuncle.core.design

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = MutedRoyalBlue,
    onPrimary = WhiteSurface,
    secondary = BrightRoyalBlue,
    background = LightBlueBg,
    onBackground = DarkNavy,
    surface = WhiteSurface,
    onSurface = DarkNavy,
    surfaceVariant = LightBlueBg,
    onSurfaceVariant = SoftGray,
    outline = DividerLight
)

private val DarkColors = darkColorScheme(
    primary = BrightRoyalBlue,
    onPrimary = InkBlack,
    secondary = MutedRoyalBlue,
    background = InkBlack,
    onBackground = WhiteSurface,
    surface = SurfaceDark,
    onSurface = WhiteSurface,
    surfaceVariant = Graphite,
    onSurfaceVariant = SoftGray,
    outline = DividerDark
)

private val LocalIsDarkTheme = staticCompositionLocalOf { false }

@Composable
fun InternshipUncleTheme(
    // Force light theme so we match the redesign aesthetic perfectly
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = InternshipUncleTypography,
        content = {
            CompositionLocalProvider(
                LocalAppSpacing provides AppSpacing(),
                LocalIsDarkTheme provides darkTheme,
                content = content
            )
        }
    )
}

object InternshipUncleTheme {
    val spacing: AppSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalAppSpacing.current

    val isDarkTheme: Boolean
        @Composable
        @ReadOnlyComposable
        get() = LocalIsDarkTheme.current
}
