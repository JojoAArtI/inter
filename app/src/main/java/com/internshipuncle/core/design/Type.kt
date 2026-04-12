package com.internshipuncle.core.design

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ── Fintech Typography System ──────────────────────────────────────────
// Inspired by the fintech dashboard screenshot:
//   • Large bold numbers for hero metrics (balance/score style)
//   • Clean weight contrast — Heavy headlines, Regular body
//   • Tight letter-spacing throughout for a modern, engineered feel
//   • System sans-serif (Roboto on Android, clean and neutral)

private val DisplayFamily = FontFamily.SansSerif
private val TextFamily    = FontFamily.SansSerif

val InternshipUncleTypography = Typography(

    // ── Hero / Balance number (like the large ₦674,981.65 in screenshot)
    displayLarge = TextStyle(
        fontFamily = DisplayFamily,
        fontWeight = FontWeight.Bold,
        fontSize   = 40.sp,
        lineHeight = 44.sp,
        letterSpacing = (-0.8).sp
    ),

    // ── Section hero  (32sp SemiBold)
    displayMedium = TextStyle(
        fontFamily = DisplayFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 32.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.5).sp
    ),

    // ── Screen-level sub-hero
    displaySmall = TextStyle(
        fontFamily = DisplayFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 26.sp,
        lineHeight = 30.sp,
        letterSpacing = (-0.3).sp
    ),

    // ── Page / Feature heading (e.g. section title in a list)
    headlineLarge = TextStyle(
        fontFamily = DisplayFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 22.sp,
        lineHeight = 26.sp,
        letterSpacing = (-0.2).sp
    ),

    // ── Card group heading
    headlineMedium = TextStyle(
        fontFamily = DisplayFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.2).sp
    ),

    // ── Inline heading / list section label
    headlineSmall = TextStyle(
        fontFamily = DisplayFamily,
        fontWeight = FontWeight.Medium,
        fontSize   = 18.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.1).sp
    ),

    // ── Card title (bold)
    titleLarge = TextStyle(
        fontFamily = DisplayFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.1).sp
    ),

    // ── Sub-card title / list item title
    titleMedium = TextStyle(
        fontFamily = DisplayFamily,
        fontWeight = FontWeight.Medium,
        fontSize   = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.05).sp
    ),

    // ── Small title / caption label
    titleSmall = TextStyle(
        fontFamily = TextFamily,
        fontWeight = FontWeight.Medium,
        fontSize   = 13.sp,
        lineHeight = 17.sp,
        letterSpacing = 0.sp
    ),

    // ── Standard body text
    bodyLarge = TextStyle(
        fontFamily = TextFamily,
        fontWeight = FontWeight.Normal,
        fontSize   = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.2).sp
    ),

    // ── Secondary body / list detail
    bodyMedium = TextStyle(
        fontFamily = TextFamily,
        fontWeight = FontWeight.Normal,
        fontSize   = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.1).sp
    ),

    // ── Caption / timestamp / micro text
    bodySmall = TextStyle(
        fontFamily = TextFamily,
        fontWeight = FontWeight.Normal,
        fontSize   = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = (-0.05).sp
    ),

    // ── Button / label — SemiBold for punchy action text
    labelLarge = TextStyle(
        fontFamily = TextFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),

    // ── Chip / badge label
    labelMedium = TextStyle(
        fontFamily = TextFamily,
        fontWeight = FontWeight.Medium,
        fontSize   = 13.sp,
        lineHeight = 17.sp,
        letterSpacing = 0.sp
    ),

    // ── Micro / footnote
    labelSmall = TextStyle(
        fontFamily = TextFamily,
        fontWeight = FontWeight.Medium,
        fontSize   = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.2.sp
    )
)
