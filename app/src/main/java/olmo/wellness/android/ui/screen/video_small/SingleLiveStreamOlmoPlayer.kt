package olmo.wellness.android.ui.screen.video_small

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView


@Composable
fun SingleOlmoPlayer(
    olmoPlayer: ExoPlayer,
    isLiveStream: Boolean,
    modifier: Modifier = Modifier
) {
    AndroidView({
        StyledPlayerView(it).apply {
            player = olmoPlayer
            useController = false
            controllerAutoShow = false
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            setShowBuffering(StyledPlayerView.SHOW_BUFFERING_NEVER)
        }
    })
}