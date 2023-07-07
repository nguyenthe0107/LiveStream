package olmo.wellness.android.ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp
import olmo.wellness.android.ui.theme.Color_Green_Main

@Composable
fun DrawDashLine() {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    Canvas(
        Modifier
            .fillMaxHeight()
            .width(1.dp)
    ) {
        drawLine(
            color = Color_Green_Main,
            strokeWidth = 5f,
            start = Offset(0f, 0f),
            end = Offset(0f, size.height),
            pathEffect = pathEffect
        )
    }
}
