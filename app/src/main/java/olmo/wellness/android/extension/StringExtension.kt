package olmo.wellness.android.extension

import androidx.compose.ui.graphics.Color

fun String.parseToColor(): Color {
    if (this[0] == '#') {
        return Color(android.graphics.Color.parseColor(this))
    }
    throw IllegalArgumentException("Unknown color")
}
