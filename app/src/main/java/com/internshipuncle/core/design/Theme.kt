package com.internshipuncle.core.design

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// ── Light (primary) color scheme — monochrome fintech ─────────────────
private val LightColors = lightColorScheme(
    primary             = InkBlack,         // Filled buttons, active elements
    onPrimary           = PureWhite,
    primaryContainer    = SurfaceGray,
    onPrimaryContainer  = InkBlack,
    secondary           = SlateGray,
    onSecondary         = PureWhite,
    secondaryContainer  = SurfaceGray,
    onSecondaryContainer  = InkBlack,
    tertiary            = SlateGray,
    onTertiary          = PureWhite,
    background          = CanvasWhite,      // Pure white canvas
    onBackground        = InkBlack,
    surface             = PureWhite,        // Card surfaces
    onSurface           = InkBlack,
    surfaceVariant      = SurfaceGray,      // Slightly off-white sections
    onSurfaceVariant    = SlateGray,
    surfaceContainerLowest    = PureWhite,
    surfaceContainerLow       = PureWhite,
    surfaceContainer          = SurfaceGray,
    surfaceContainerHigh      = SurfaceLight,
    surfaceContainerHighest   = SurfaceLight,
    outline             = DividerGray,
    outlineVariant      = DividerGray,
    error               = ErrorRed,
    onError             = PureWhite,
    errorContainer      = Color(0xFFFFE5E3),
    onErrorContainer    = Color(0xFF410E0B),
    inverseSurface      = CharcoalDark,
    inverseOnSurface    = PureWhite,
    inversePrimary      = SurfaceGray,
    scrim               = ScrimDark
)

// ── Dark color scheme (minimal — kept for compat) ──────────────────────
private val DarkColors = darkColorScheme(
    primary             = SurfaceGray,
    onPrimary           = InkBlack,
    primaryContainer    = CharcoalDark,
    onPrimaryContainer  = SurfaceGray,
    secondary           = SilverMist,
    onSecondary         = InkBlack,
    background          = Color(0xFF111113),
    onBackground        = PureWhite,
    surface             = Color(0xFF1C1C1E),
    onSurface           = PureWhite,
    surfaceVariant      = Color(0xFF2C2C2E),
    onSurfaceVariant    = SilverMist,
    outline             = Color(0xFF48484A),
    outlineVariant      = Color(0xFF3A3A3C),
    error               = ErrorRed,
    onError             = PureWhite,
    errorContainer      = Color(0xFF93000A),
    onErrorContainer    = Color(0xFFFFDAD6)
)

// ── Shape system — matches fintech rounded-rect aesthetic ──────────────
private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small      = RoundedCornerShape(12.dp),
    medium     = RoundedCornerShape(16.dp),
    large      = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

private val LocalIsDarkTheme = staticCompositionLocalOf { false }

@Composable
fun InternshipUncleTheme(
    darkTheme: Boolean = false,         // Force light mode; matches screenshot
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography  = InternshipUncleTypography,
        shapes      = AppShapes,
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
