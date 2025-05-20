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

// Yeni renkleri import et
// import com.aliaktas.taskme.ui.theme.* // Eğer Color.kt'deki tüm renkleri kullanacaksanız

// Açık tema renk şeması (YENİLENDİ)
private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White, // Ana renk üzerindeki metin/icon
    primaryContainer = PrimaryLightBlue,
    onPrimaryContainer = PrimaryDarkBlue,
    secondary = SecondaryBlue,
    onSecondary = Color.White, // İkincil renk üzerindeki metin/icon
    secondaryContainer = SecondaryLightBlue,
    onSecondaryContainer = SecondaryDarkBlue,
    tertiary = Color(0xFF00C853), // Örnek bir üçüncül renk (Belki olumlu aksiyonlar için)
    onTertiary = Color.White,
    background = BackgroundLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = Color(0xFFE0E8F0), // Yüzeyin hafif farklı tonu
    onSurfaceVariant = TextSecondaryLight,
    outline = Color(0xFFC_5D_5E_7), // Kenarlıklar için
    error = Color(0xFFD32F2F),
    onError = Color.White
)

// Koyu tema renk şeması (İsteğe bağlı olarak bunu da mavi tonlarına göre güncelleyebilirsin)
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryLightBlue, // Koyu tema için daha açık birincil
    onPrimary = PrimaryDarkBlue,
    primaryContainer = PrimaryDarkBlue,
    onPrimaryContainer = PrimaryLightBlue,
    secondary = SecondaryLightBlue,
    onSecondary = SecondaryDarkBlue,
    secondaryContainer = SecondaryDarkBlue,
    onSecondaryContainer = SecondaryLightBlue,
    background = Color(0xFF121A23), // Koyu Mavi Arka Plan
    onBackground = Color(0xFFE0E8F0),
    surface = Color(0xFF1A2530),    // Koyu Mavi Yüzey
    onSurface = Color(0xFFD_0D_9E_3),
    // ... diğer renkleri de benzer şekilde uyarla
)

@Composable
fun TaskMeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Status bar rengini arka planla veya ana renkle uyumlu yapabiliriz.
            // AppBar kaldırılacağı için arka plan rengi daha uygun olabilir.
            window.statusBarColor = colorScheme.background.toArgb() // VEYA colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Typography'i bir sonraki adımda güncelleyeceğiz
        content = content
    )
}