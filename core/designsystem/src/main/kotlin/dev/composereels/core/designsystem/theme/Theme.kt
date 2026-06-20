package dev.composereels.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// A reels feed is always shown over dark video frames, so the app ships a single dark scheme
// rather than following the system light/dark setting.
private val ReelsColorScheme = darkColorScheme(
    primary = Magenta,
    onPrimary = OffWhite,
    primaryContainer = MagentaDark,
    secondary = Cyan,
    background = NearBlack,
    onBackground = OffWhite,
    surface = NearBlack,
    onSurface = OffWhite,
    surfaceVariant = Grey,
)

/** The single Material 3 theme used across the app. */
@Composable
fun ComposeReelsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ReelsColorScheme,
        typography = ReelsTypography,
        content = content,
    )
}
