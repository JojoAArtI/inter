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
    primary = AccentBlue,
    onPrimary = Cloud,
    secondary = LinkBlue,
    background = Mist,
    onBackground = Graphite,
    surface = Cloud,
    onSurface = Graphite,
    surfaceVariant = Mist,
    onSurfaceVariant = Slate,
    outline = DividerLight
)

private val DarkColors = darkColorScheme(
    primary = BrightBlue,
    onPrimary = InkBlack,
    secondary = AccentBlue,
    background = InkBlack,
    onBackground = Cloud,
    surface = SurfaceDark,
    onSurface = Cloud,
    surfaceVariant = Graphite,
    onSurfaceVariant = Color(0xFFD2D2D7),
    outline = DividerDark
)

private val LocalIsDarkTheme = staticCompositionLocalOf { false }

@Composable
fun InternshipUncleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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
