package com.aliaktas.taskme.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.aliaktas.taskme.R // R dosyasını import etmeyi unutma

// Poppins Font Ailesini Tanımla
val PoppinsFamily = FontFamily(
    Font(R.font.poppins_lightitalic, FontWeight.Light, FontStyle.Italic), // Eğer light italic varsa
    Font(R.font.poppins_regular, FontWeight.Normal), // Normal ağırlık için regular ekledim, yoksa medium da olabilir
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_bold, FontWeight.Bold),
    Font(R.font.poppins_black, FontWeight.Black) // veya ExtraBold/Heavy
    // Eğer sadece belirttiğin fontlar varsa:
    // Font(R.font.poppins_lightitalic, FontWeight.Light, FontStyle.Italic),
    // Font(R.font.poppins_medium, FontWeight.Medium),
    // Font(R.font.poppins_bold, FontWeight.Bold),
    // Font(R.font.poppins_black, FontWeight.Black),
)

// Uygulama tipografi ayarları (YENİLENDİ - Poppins ile)
val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = PoppinsFamily,
        fontWeight = FontWeight.Bold, // veya FontWeight.Black
        fontSize = 30.sp,
        lineHeight = 38.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = PoppinsFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 34.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = PoppinsFamily,
        fontWeight = FontWeight.Bold, // veya Medium
        fontSize = 22.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle( // DayCard gün adı için kullanılabilir
        fontFamily = PoppinsFamily,
        fontWeight = FontWeight.Medium, // veya Bold
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.15.sp
    ),
    titleMedium = TextStyle( // TaskCard başlığı için kullanılabilir
        fontFamily = PoppinsFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle( // Genel metinler
        fontFamily = PoppinsFamily,
        fontWeight = FontWeight.Normal, // veya Medium
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = PoppinsFamily,
        fontWeight = FontWeight.Normal, // veya Medium
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    labelLarge = TextStyle( // Butonlar ve önemli etiketler
        fontFamily = PoppinsFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle( // DayCard görev sayısı gibi küçük etiketler
        fontFamily = PoppinsFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = PoppinsFamily,
        fontWeight = FontWeight.Light, // Belki light italic burada kullanılabilir
        fontStyle = FontStyle.Italic, // Eğer poppins_lightitalic varsa
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)