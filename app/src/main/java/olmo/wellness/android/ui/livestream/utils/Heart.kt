package olmo.wellness.android.ui.livestream.utils

import android.graphics.Canvas
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import olmo.wellness.android.ui.theme.Color_Background_Heart_1
import olmo.wellness.android.ui.theme.White
import kotlin.random.Random.Default.nextInt

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Heart(
    modifier: Modifier,
    horizontalPadding: Int,
    bottomMargin: Int,
    offsetX: Int,
    offsetY: Int,
    color: Color = Color_Background_Heart_1,
    number: Int
) {
    val width = LocalConfiguration.current.screenWidthDp
    val height = LocalConfiguration.current.screenHeightDp - bottomMargin

    val yRandom = nextInt(0, height / 2)
    val xRandom = nextInt(horizontalPadding, (width - horizontalPadding))


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

    val rotateCircleAnimation by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = getRotate(number),
        animationSpec = infiniteRepeatable(
            animation = tween(1000, delayMillis = 100, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        )
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
        Canvas(modifier = modifier
            .offset(y = offsetYAnimation, x = offsetXAnimation),
            onDraw = {
                val path = Path().apply {
                    heartPath(sizeSmall)
                }

                val pathCircle = Path().apply {
                    circlePath(sizeSmall, sizeLage)
                }
                drawPath(
                    path = pathCircle,
                    color = color
                )
                rotate(
                    degrees = rotateCircleAnimation,
                    pivot = Offset(sizeSmall.width / 2, sizeSmall.height / 2)
                ) {
                    drawPath(
                        path = path,
                        color = White,
                    )
                }
            }
        )
    }
}

enum class HeartState {
    Show,
    Hide
}

private fun getRotate(number: Int): Float {

    return when (number % 10) {
        1, 5, 8 -> 360f
        1, 9 -> 270f
        2, 7 -> -235f
        else -> 0f
    }
}


fun Path.circlePath(sizeSmall: Size, sizeLarge: Size): Path {
    addOval(
        Rect(
            Offset(
                (sizeSmall.width - sizeLarge.width) / 2,
                (sizeSmall.height - sizeLarge.height) / 2
            ), sizeLarge
        )
    )
    return this
}

fun Path.heartPath(size: Size): Path {

    val width: Float = size.width
    val height: Float = size.height

    // Starting point
    moveTo(width / 2, height / 5)

    // Upper left path
    cubicTo(
        5 * width / 14, 0f,
        0f, height / 15,
        width / 28, 2 * height / 5
    )

    // Lower left path
    cubicTo(
        width / 14, 2 * height / 3,
        3 * width / 7, 5 * height / 6,
        width / 2, height
    )

    // Lower right path
    cubicTo(
        4 * width / 7, 5 * height / 6,
        13 * width / 14, 2 * height / 3,
        27 * width / 28, 2 * height / 5
    )

    // Upper right path
    cubicTo(
        width, height / 15,
        9 * width / 14, 0f,
        width / 2, height / 5
    )
    return this
}