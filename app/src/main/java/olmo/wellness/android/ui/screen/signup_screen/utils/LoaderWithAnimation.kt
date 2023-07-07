package olmo.wellness.android.ui.screen.signup_screen.utils

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color

@Composable
fun LoaderWithAnimation(isPlaying: Boolean) {
    val infiniteTransition = rememberInfiniteTransition()

    val pxValue = with(LocalDensity.current) { 30.dp.toPx() }
    val pxOffset = with(LocalDensity.current) { 5.dp.toPx() }

    val arcAngle1 by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 180F,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val arcAngle2 by infiniteTransition.animateFloat(
        initialValue = 180F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val greenCircleAnimation by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, delayMillis = 100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val res= ImageVector.vectorResource(id = R.drawable.olmo_img_onboard_right)
    val paint = rememberVectorPainter(image = res)

    if (isPlaying) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .size(40.dp)
            ) {
                drawArc(
                    color = Color_LiveStream_Main_Color,
                    startAngle = arcAngle1,
                    sweepAngle = 90f,
                    useCenter = false,
                    style = Stroke(width = 5f, cap = StrokeCap.Round),
                )

                drawArc(
                    color = Color_LiveStream_Main_Color,
                    startAngle = arcAngle2,
                    sweepAngle = 90f,
                    useCenter = false,
                    style = Stroke(width = 5f, cap = StrokeCap.Round),
                )
                translate(left = pxOffset, top = pxOffset) {
                    with(paint) {
                        draw(
                            paint.intrinsicSize.copy(
                                width = pxValue,
                                height = pxValue,
                            ),
                            alpha = greenCircleAnimation
                        )
                    }
                }
            }
        }
    }
}