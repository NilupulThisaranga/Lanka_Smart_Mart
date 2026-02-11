package com.lankasmartmart.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SaffronOrange,
    onPrimary = BackgroundDark,
    primaryContainer = SaffronOrangeDark,
    onPrimaryContainer = OffWhite,
    
    secondary = EmeraldGreen,
    onSecondary = BackgroundDark,
    secondaryContainer = MaroonRed,
    onSecondaryContainer = OffWhite,
    
    tertiary = GoldenYellow,
    onTertiary = BackgroundDark,
    
    background = BackgroundDark,
    onBackground = OffWhite,
    
    surface = SurfaceDark,
    onSurface = OffWhite,
    surfaceVariant = DarkGray,
    onSurfaceVariant = LightGray,
    
    error = ErrorRed,
    onError = BackgroundDark,
    
    outline = MediumGray
)

private val LightColorScheme = lightColorScheme(
    primary = SaffronOrange,
    onPrimary = SurfaceLight,
    primaryContainer = SaffronOrangeDark,
    onPrimaryContainer = SurfaceLight,
    
    secondary = EmeraldGreen,
    onSecondary = SurfaceLight,
    secondaryContainer = MaroonRed,
    onSecondaryContainer = OffWhite,
    
    tertiary = GoldenYellow,
    onTertiary = DarkGray,
    
    background = BackgroundLight,
    onBackground = DarkGray,
    
    surface = SurfaceLight,
    onSurface = DarkGray,
    surfaceVariant = LightGray,
    onSurfaceVariant = DarkGray,
    
    error = ErrorRed,
    onError = SurfaceLight,
    
    outline = MediumGray
)

@Composable
fun LankaSmartMartTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
