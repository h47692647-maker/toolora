package com.example.ui.theme

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

private val DarkColorScheme = darkColorScheme(
  primary = TooloraPrimary,
  onPrimary = Color.Black,
  primaryContainer = TooloraPrimaryVariant,
  onPrimaryContainer = Color.White,
  secondary = TooloraSecondary,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFF4C1D95),
  onSecondaryContainer = Color(0xFFDDD6FE),
  tertiary = TooloraTertiary,
  background = TooloraNavyDark,
  onBackground = TooloraDarkText,
  surface = TooloraSlateDark,
  onSurface = TooloraDarkText,
  surfaceVariant = TooloraSurfaceDark,
  onSurfaceVariant = TooloraDarkTextMuted,
  outline = Color(0xFF475569)
)

private val LightColorScheme = lightColorScheme(
  primary = TooloraPrimaryVariant,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFE0F2FE),
  onPrimaryContainer = Color(0xFF0369A1),
  secondary = TooloraSecondary,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFEDE9FE),
  onSecondaryContainer = Color(0xFF5B21B6),
  tertiary = TooloraTertiary,
  background = TooloraLightBg,
  onBackground = TooloraLightText,
  surface = TooloraLightSurface,
  onSurface = TooloraLightText,
  surfaceVariant = TooloraLightSurfaceVariant,
  onSurfaceVariant = TooloraLightTextMuted,
  outline = Color(0xFFCBD5E1)
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep Toolora branded colors consistent
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
