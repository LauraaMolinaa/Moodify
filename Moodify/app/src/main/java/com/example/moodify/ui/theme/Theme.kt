package com.example.moodify.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.font.FontWeight


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
        typography = Typography,
        content = content
    )
}