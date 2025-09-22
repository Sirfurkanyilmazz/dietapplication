package com.dietapplication.view.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Shapes

private val Mint = Color(0xFF00C2A8)
private val MintDark = Color(0xFF00A38E)
private val SurfaceDark = Color(0xFF101314)
private val SurfaceVariantDark = Color(0xFF171A1B)

private val DarkScheme = darkColorScheme(
    primary = Mint,
    onPrimary = Color.Black,
    primaryContainer = MintDark,
    onPrimaryContainer = Color(0xFF00110E),
    secondary = Color(0xFF8BD5C8),
    onSecondary = Color.Black,
    background = SurfaceDark,
    onBackground = Color(0xFFE9F2F0),
    surface = SurfaceDark,
    onSurface = Color(0xFFE9F2F0),
    surfaceVariant = SurfaceVariantDark
)

private val LightScheme = lightColorScheme(
    primary = Color(0xFF0F9B8E),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBFEDE6),
    onPrimaryContainer = Color(0xFF00201C),
    secondary = Color(0xFF2D7A73),
    onSecondary = Color.White
)

val AppShapes = Shapes(
    extraSmall = androidx.compose.foundation.shape.RoundedCornerShape(10),
    small = androidx.compose.foundation.shape.RoundedCornerShape(14),
    medium = androidx.compose.foundation.shape.RoundedCornerShape(18),
    large = androidx.compose.foundation.shape.RoundedCornerShape(22),
    extraLarge = androidx.compose.foundation.shape.RoundedCornerShape(28)
)

@Composable
fun AppTheme(useDark: Boolean = true, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (useDark) DarkScheme else LightScheme,
        shapes = AppShapes,
        typography = Typography(),
        content = content
    )
}
