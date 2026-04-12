package com.internshipuncle.core.design

import androidx.compose.ui.graphics.Color

// ── Fintech Monochrome Palette ─────────────────────────────────────────
// Inspired by clean fintech dashboard: white canvas, black/gray hierarchy,
// no blue accents — purely monochrome with semantic status colors only.

// ── Canvas & Surfaces ──────────────────────────────────────────────────
val CanvasWhite   = Color(0xFFFFFFFF)   // Main app background
val SurfaceGray   = Color(0xFFF5F5F7)   // Cards, sections, input fields
val SurfaceLight  = Color(0xFFF0F0F3)   // Slightly deeper card bg
val PureWhite     = Color(0xFFFFFFFF)   // Stark white (alias)

// ── Text Hierarchy ─────────────────────────────────────────────────────
val InkBlack      = Color(0xFF1A1A1A)   // Primary text, filled buttons (near-black)
val CharcoalDark  = Color(0xFF2C2C2E)   // Dark nav pill, dark accents
val SlateGray     = Color(0xFF6B6B6B)   // Secondary text, sublabels
val SilverMist    = Color(0xFFAEAEB2)   // Tertiary, placeholders, disabled
val DividerGray   = Color(0xFFE5E5EA)   // Subtle horizontal dividers

// ── Interactive / Buttons ──────────────────────────────────────────────
val NavPillDark   = Color(0xFF1C1C1E)   // Dark floating bottom nav pill
val NavIconWhite  = Color(0xFFFFFFFF)   // Icons on dark nav
val NavSelectedBg = Color(0xFFFFFFFF)   // Selected tab circle on dark nav
val BtnOutlineStroke = Color(0xFFD1D1D6) // Outlined circular button border

// ── Status / Semantic ──────────────────────────────────────────────────
val GreenPositive = Color(0xFF34C759)   // Positive trend, success state
val RedNegative   = Color(0xFFFF3B30)   // Negative trend, error state
val AmberWarning  = Color(0xFFFF9500)   // Warning state
val InfoCharcoal  = Color(0xFF3A3A3C)   // Informational on dark background

// ── Overlay & Scrim ────────────────────────────────────────────────────
val ScrimDark     = Color(0x66000000)
val GlassWhite    = Color(0xCCFFFFFF)

// ── Legacy compatibility aliases (point at new palette) ────────────────
val Cloud            = PureWhite
val DarkNavy         = CharcoalDark
val LightBlueBg      = SurfaceGray      // was SkyBlueLight
val WhiteSurface     = PureWhite
val MutedRoyalBlue   = InkBlack         // was RoyalBlue → now ink
val BrightRoyalBlue  = SlateGray        // was SoftBlue  → now neutral
val RoyalBlue        = InkBlack         // Primary interactive = InkBlack
val DeepNavy         = CharcoalDark
val SoftBlue         = SlateGray
val PaleBlue         = SurfaceGray
val SkyBlueLight     = SurfaceGray
val SkyBlueMedium    = SurfaceLight
val FrostWhite       = PureWhite
val CoolGray         = SlateGray
val Graphite         = InkBlack
val MistGray         = DividerGray
val SoftGray         = SilverMist
val DividerLight     = DividerGray
val DividerDark      = Color(0xFF3A3A3C)
val SurfaceDark      = Color(0xFF272729)
val SuccessGreen     = GreenPositive
val WarningAmber     = AmberWarning
val ErrorRed         = RedNegative
val InfoBlue         = InfoCharcoal
val NavPillLight     = SurfaceGray
val NavSurfaceGlass  = SurfaceGray
