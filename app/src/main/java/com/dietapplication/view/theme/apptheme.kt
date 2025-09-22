package com.dietapplication.view.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Shapes
import androidx.compose.foundation.shape.RoundedCornerShape

private val Mint = Color(0xFF00C2A8)
private val MintDark = Color(0xFF00A38E)
private val SurfaceDark = Color(0xFF101314)
private val SurfaceVar = Color(0xFF171A1B)

private val Dark = darkColorScheme(
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
    surfaceVariant = SurfaceVar,
    onSurfaceVariant = Color(0xFFC9D7D4),
    outline = Color(0xFF2B3132)
)

private val Light = lightColorScheme(
    primary = Color(0xFF0F9B8E),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBFEDE6),
    onPrimaryContainer = Color(0xFF00201C),
    secondary = Color(0xFF2D7A73),
    onSecondary = Color.White
)

val AppShapes = Shapes(
    small = RoundedCornerShape(14),
    medium = RoundedCornerShape(18),
    large = RoundedCornerShape(24),
    extraLarge = RoundedCornerShape(28)
)

@Composable
fun AppTheme(useDark: Boolean = true, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (useDark) Dark else Light,
        shapes = AppShapes,
        typography = Typography(),
        content = content
    )
}
