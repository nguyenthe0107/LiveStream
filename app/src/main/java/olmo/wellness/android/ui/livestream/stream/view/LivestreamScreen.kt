package olmo.wellness.android.ui.livestream.stream.view

import android.annotation.SuppressLint
import android.widget.RelativeLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import olmo.wellness.android.data.model.live_stream.LiveStreamRequest
import olmo.wellness.android.ui.livestream.utils.Effects
import olmo.wellness.android.ui.livestream.view.streamer.LiveStreamerViewModel

@SuppressLint("NotifyDataSetChanged", "MutableCollectionMutableState",
    "CoroutineCreationDuringComposition"
)
@Composable
fun LivestreamScreen(
    navController: NavController,
    viewModel: LiveStreamerViewModel = hiltViewModel(),
){
    var scope = rememberCoroutineScope()

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    Effects.Disposable(
        lifeCycleOwner = lifecycleOwner,
        onStart = {
//            viewModel.setupBroadcastSession(context)
        },
        onStop = {
            viewModel.stopLive()
        }
    )

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier.background(Color.White),
            text = if (viewModel.liveStreaming) "Livestreaming at channel ${viewModel.livestreamId}" else "Livestreaming"
        )
        AndroidView(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(Color.Green),
            factory = { ctx ->
                RelativeLayout(ctx).apply {

                }
            },
            update = {
                val view = viewModel.previewView

//                println("update preview view")
//                val notNull = view != null
//                println("$notNull")
//                if (view == null) {
//                    it.removeAllViews()
//                } else {
//                    if (view.parent == null) {
//                        it.addView(view)
//                    }
//                }
            }
        )

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(
                enabled = !viewModel.liveStreaming,
                onClick = {
                    viewModel.startLive(LiveStreamRequest(title = "test_live_olmo"),{

                    })
                }) {
                Text("Live")
            }
            Button(
                onClick = {
                    navController.popBackStack()
                }) {
                Text("Stop")
            }
        }

    }

}

