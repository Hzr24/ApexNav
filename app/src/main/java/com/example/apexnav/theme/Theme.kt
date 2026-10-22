package com.example.apexnav.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val ApexNavColorScheme = darkColorScheme(
    primary          = ApexCyan,
    onPrimary        = SurfaceDark,
    primaryContainer = SurfaceElevated,
    secondary        = ApexEmerald,
    onSecondary      = SurfaceDark,
    tertiary         = ApexAmber,
    background       = SurfaceDark,
    surface          = SurfacePanel,
    surfaceVariant   = SurfaceCard,
    onBackground     = TextPrimary,
    onSurface        = TextPrimary,
    onSurfaceVariant = TextSecondary,
    error            = ApexCrimson,
    outline          = BorderSubtle,
    outlineVariant   = BorderAccent,
)

@Composable
fun ApexNavTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ApexNavColorScheme,
        typography  = Typography,
        content     = content,
    )
}
