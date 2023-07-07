package olmo.wellness.android.ui.common

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import olmo.wellness.android.ui.theme.White

@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    height: Dp,
    radius: Dp = 16.dp,
    shimmerColor: Color = Color.LightGray,
    backgroundColor: Color = White,

) {
    val shimmerColors = listOf(
        backgroundColor.copy(alpha = 0.6f),
        shimmerColor.copy(alpha = 0.6f),
        backgroundColor,
    )
    val transition = remember { Animatable(0f) }
    LaunchedEffect(transition){
        withContext(Dispatchers.IO){
            transition.animateTo(
                targetValue = 1000f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 800,
                        delayMillis = 300,
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
    }
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = 0f, y = 0f),
        end = Offset(x = transition.value, y = transition.value)
    )

    Box(
        modifier = modifier
            .background(
                brush,
                shape = RoundedCornerShape(radius)
            )
            .fillMaxWidth()
            .height(height)

    ) {

    }
}