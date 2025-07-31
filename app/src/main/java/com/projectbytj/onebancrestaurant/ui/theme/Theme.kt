package com.projectbytj.onebancrestaurant.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val GreenPrimary = Color(0xFF4CAF50)  // fresh green
val TealSecondary = Color(0xFF009688) // teal accent
val BackgroundGrey = Color(0xFFF5F5F5)
val SurfaceWhite = Color(0xFFFFFFFF)
val TextDark = Color(0xFF212121)
val LightGreenTertiary = Color(0xFF8BC34A)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xF57C00FF),
    onPrimary = Color.White,
    secondary = TealSecondary,
    onSecondary = Color.White,
    background = BackgroundGrey,
    onBackground = TextDark,
    surface = SurfaceWhite,
    onSurface = TextDark,
    tertiary = LightGreenTertiary,
    onTertiary = Color.White,
    error = Color(0xFFD32F2F),
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xF57C00FF),
    onPrimary = Color.White,
    secondary = TealSecondary,
    onSecondary = Color.White,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    tertiary = LightGreenTertiary,
    onTertiary = Color.White,
    error = Color(0xFFD32F2F),
    onError = Color.White
)

@Composable
fun OnebancRestaurantTheme(
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

        darkTheme -> LightColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}