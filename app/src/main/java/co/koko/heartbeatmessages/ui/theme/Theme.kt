// co/koko/heartbeatmessages/ui/theme/Theme.kt

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import co.koko.heartbeatmessages.ui.components.*

private val DarkColorScheme = darkColorScheme(
    primary = Pink80,
    onPrimary = TextColorDark,
    secondary = Pink80,
    tertiary = Pink40
)

private val LightColorScheme = lightColorScheme(
    primary = Pink40,
    onPrimary = TextColorLight,
    secondary = Pink80,
    tertiary = LightPink,
    background = BackgroundPink, // 전체 화면 배경색
    surface = Color.White,   // 카드 배경색
    onSurface = TextColorDark,
    error = ErrorRed,
    onError = OnError,
    outline = OutlineGray,
    inverseSurface = InverseSurface,
    inverseOnSurface = InverseOnSurface
)

@Composable
fun HeartbeatMessagesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}