package olmo.wellness.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
        primary = Color_LiveStream_Main_Color,
        primaryVariant = Color_LiveStream_Main_Color,
        secondary = Color_LiveStream_Main_Color
)

private val LightColorPalette = lightColors(
    primary = Color_LiveStream_Main_Color,
    primaryVariant = Color_LiveStream_Main_Color,
    secondary = Color_LiveStream_Main_Color
)

@Composable
fun OlmoAndroidTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
//    val colors = if (darkTheme) {
//        DarkColorPalette
//    } else {
//        LightColorPalette
//    }
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    MaterialTheme(
            colors = LightColorPalette,
            typography = Typography,
            shapes = Shapes,
            content = content
    )
}