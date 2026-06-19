package com.nyx.chat.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val RedTeamColorScheme = darkColorScheme(
    primary      = RedTeamRed,
    secondary    = Accent,
    tertiary     = TerminalGreen,
    surface      = DarkSurface,
    background   = DarkBackground,
    error        = WarningAmber,
    onPrimary    = Color.White,
    onSurface    = Color.White,
    onBackground = Color.White,
    onError      = Color.Black
)

/**
 * Red Team AI theme — pitch black background, danger red accents, neon green terminal feel.
 */
@Composable
fun RedTeamTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = android.graphics.Color.parseColor("#09090F")
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
    MaterialTheme(
        colorScheme = RedTeamColorScheme,
        typography  = Typography,
        content     = content
    )
}
