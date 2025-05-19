package com.aliaktas.taskme.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Açık tema renk şeması
private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    primaryContainer = PrimaryLightColor,
    onPrimaryContainer = PrimaryDarkColor,
    secondary = SecondaryColor,
    secondaryContainer = SecondaryLightColor,
    onSecondaryContainer = SecondaryDarkColor,
    background = BackgroundColor,
    surface = SurfaceColor
)

// Koyu tema renk şeması (şu an sadece açık temaya odaklanıyoruz)
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryLightColor,
    primaryContainer = PrimaryColor,
    onPrimaryContainer = PrimaryLightColor,
    secondary = SecondaryLightColor,
    secondaryContainer = SecondaryColor,
    onSecondaryContainer = SecondaryLightColor,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E)
)

@Composable
fun TaskMeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Tema renk şemasını belirle
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    // Status bar rengini tema ile uyumlu hale getir
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    // MaterialTheme ile renk şeması, tipografi ve şekil ayarlarını uygula
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}