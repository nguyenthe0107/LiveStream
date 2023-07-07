package olmo.wellness.android.ui.screen.playback_video.bottom_sheet.voucher_service

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color
import olmo.wellness.android.ui.theme.Neutral_Gray_6
import olmo.wellness.android.ui.theme.Neutral_Gray_7
import olmo.wellness.android.ui.theme.White

@Composable
fun TicketCompose(isAvailable: Boolean = false, content: @Composable() () -> Unit) {
    val colorBackground = if(isAvailable) Color_LiveStream_Main_Color else Neutral_Gray_7
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .height(120.dp)){

        content()

        Box(
            modifier = Modifier
                .size(28.dp)
                .offset((-14).dp, 0.dp)
                .background(color = White, shape = CircleShape)
                .align(Alignment.CenterStart)){
        }

        Box(
            modifier = Modifier
                .size(28.dp)
                .offset((14).dp, 0.dp)
                .background(color = White, shape = CircleShape)
                .align(Alignment.CenterEnd)){

        }

        Box(
            modifier = Modifier
                .size(28.dp)
                .offset((14).dp, 0.dp)
                .background(color = White, shape = CircleShape)
                .align(Alignment.CenterEnd)){
            val sliderValue by remember { mutableStateOf(0.5f) }
            Canvas(
                modifier = Modifier
                    .size(28.dp)
            ){
                drawCircle(
                    SolidColor(colorBackground),
                    size.width / 2,
                    style = Stroke(2f)
                )
                val convertedValue = sliderValue * 360
                drawArc(
                    colorFilter = ColorFilter.tint(color = White),
                    brush = SolidColor(Color.White),
                    startAngle = -90f,
                    sweepAngle = convertedValue,
                    useCenter = false,
                    style = Stroke(3f)
                )
            }
        }

    }
}