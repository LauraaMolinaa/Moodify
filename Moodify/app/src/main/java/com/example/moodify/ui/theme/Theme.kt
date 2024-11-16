package com.example.moodify.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.moodify.R


// Define Kalam Font for Tile
val kalamFontFamily = FontFamily(
    Font(R.font.kalam_regular, FontWeight.Normal),
    Font(R.font.kalam_bold, FontWeight.Bold)
)


//Typography using Kalam font
val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = kalamFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    titleLarge = TextStyle(
        fontFamily = kalamFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    )
)


private val LightColors = lightColorScheme(
    primary = Color(0xFFB793D6),
    onPrimary = Color.White,
    background = Color(0xFFE0C3FC),
    onBackground = Color(0xFF4A4A4A),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF624A7E),
    onPrimary = Color.White,
    background = Color(0xFF1F1B24),
    onBackground = Color(0xFFE6E1E5),
)


@Composable
fun MoodifyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = content
    )
}