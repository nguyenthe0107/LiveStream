package olmo.wellness.android.ui.screen.signup_screen.utils

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color
import olmo.wellness.android.ui.theme.Neutral_Gray_8

@SuppressLint("ComposableNaming")
@Composable
fun LoadingScreen(
    isLoading: Boolean,
    content: @Composable (() -> Unit)? = null,
    backgroundColor: Color = Neutral_Gray_8.copy(alpha = 0.4f)
) {
    AnimatedVisibility(
        modifier = Modifier
            .fillMaxSize(),
        visible = isLoading,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = backgroundColor)
                .pointerInput(Unit) {}) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = Color_LiveStream_Main_Color
                )
            }
        }
    }
}