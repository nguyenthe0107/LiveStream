package olmo.wellness.android.ui.livestream.stream.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView
import olmo.wellness.android.ui.livestream.utils.Effects
import olmo.wellness.android.ui.livestream.stream.viewmodel.LivestreamViewModel

class Listener(private var player: Player) : Player.Listener {
    override fun onIsPlayingChanged(isPlaying: Boolean) {
        print("is playing $isPlaying")
    }
}

@Composable
fun LivestreamView(
    navigator: NavHostController,
    streamId: Int,
    viewModel: LivestreamViewModel = hiltViewModel()
) {
    val lifeCycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val stream = viewModel.livestreamList.firstOrNull { s -> s.id == streamId }

    val context = LocalContext.current
    val playerView = remember { StyledPlayerView(context) }
    val player = remember(context) { ExoPlayer.Builder(context).build() }
    val listener = remember(player) { Listener(player) }

    Effects.Disposable(
        lifeCycleOwner = lifeCycleOwner,
        onStart = {
            player.addListener(listener)

            playerView.useController = false

            //-- fit the player to parent
            playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT

            playerView.player = player

            if (stream != null) {
                val mediaItem = stream.playbackUrl?.let { MediaItem.fromUri(it) }
                mediaItem?.let { player.addMediaItem(it) }
                player.prepare()
                player.play()
            }
        },
        onStop = {
            player.pause()
            player.removeListener(listener)
            player.release()
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .weight(1.0f),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Watching livestream $streamId")
            //-- player here
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                factory = {
                    playerView
                },
                update = {

                }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Button(onClick = {
                //-- go back
                navigator.popBackStack()
            }) {
                Text("Back")
            }
        }
    }
}