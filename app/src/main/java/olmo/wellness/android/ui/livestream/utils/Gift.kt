package olmo.wellness.android.ui.livestream.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import olmo.wellness.android.ui.screen.playback_video.donate.data.TipsType
import kotlin.random.Random

@Composable
fun Gift(
    modifier: Modifier,
    horizontalPadding: Int,
    bottomMargin: Int,
    offsetX: Int,
    offsetY: Int,
    type: TipsType
) {
    val width = LocalConfiguration.current.screenWidthDp
    val height = LocalConfiguration.current.screenHeightDp - bottomMargin

    val yRandom = Random.nextInt(0, height / 2)
    val xRandom = Random.nextInt(horizontalPadding, (width - horizontalPadding))


    val sizeSmall = Size(70f, 70f)
    val sizeLage = Size(120f, 120f)

    val state = remember {
        mutableStateOf(HeartState.Show)
    }

    val offsetYAnimation: Dp by animateDpAsState(
        targetValue = when (state.value) {
            HeartState.Show -> (offsetY).dp
            else -> yRandom.dp
        },
        animationSpec = tween(4000)
    )

    val offsetXAnimation: Dp by animateDpAsState(
        targetValue = when (state.value) {
            HeartState.Show -> (offsetX).dp
            else -> xRandom.dp
        },
        animationSpec = tween(4000)
    )

    LaunchedEffect(key1 = state, block = {
        state.value = when (state.value) {
            HeartState.Show -> HeartState.Hide
            HeartState.Hide -> HeartState.Show
        }
    })

    AnimatedVisibility(
        visible = state.value == HeartState.Show,
        enter = fadeIn(animationSpec = tween(durationMillis = 6550)),
        exit = fadeOut(animationSpec = tween(durationMillis = 6400))
    ) {
        val res = ImageVector.vectorResource(id = type.resource)
        val paint = rememberVectorPainter(image = res)
        Canvas(modifier = modifier
            .offset(y = offsetYAnimation, x = offsetXAnimation),
            onDraw = {
                with(paint) {
                    draw(paint.intrinsicSize)
                }
            }
        )
    }
}