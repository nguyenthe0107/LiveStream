package olmo.wellness.android.ui.common.animation

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*


@Composable
fun SlideInHorizontally(view: Unit, visible: Boolean){
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(animationSpec = tween(durationMillis = 200)) { fullWidth ->
            // Offsets the content by 1/3 of its width to the left, and slide towards right
            // Overwrites the default animation with tween for this slide animation.
            -fullWidth / 3
        } + fadeIn(
            // Overwrites the default animation with tween
            animationSpec = tween(durationMillis = 200)
        ),
        exit = slideOutHorizontally(animationSpec = spring(stiffness = Spring.StiffnessHigh)) {
            // Overwrites the ending position of the slide-out to 200 (pixels) to the right
            200
        } + fadeOut()
    ) {
        view
    }}