package olmo.wellness.android.ui.screen.playback_video.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.theme.Color_Background_Heart_1

@Composable
fun LikeIcon(id: Int, isLike: Boolean?, onCallbackFunc: ((Boolean) -> Unit)? = null) {
    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(isLike) {
        animatedProgress.animateTo(
            targetValue = 1.3f,
            animationSpec = tween(600),
        )
    }
    Icon(
        painter = painterResource(id = R.drawable.olmo_ic_heart_white_with_border),
        contentDescription = null,
        modifier = Modifier
            .size(32.dp)
            .noRippleClickable {
                onCallbackFunc?.invoke(true)
            }
            .graphicsLayer(scaleX = animatedProgress.value, scaleY = animatedProgress.value),
        tint = animateColorAsState(if (isLike == true) Color_Background_Heart_1 else Color.Unspecified).value
    )
}