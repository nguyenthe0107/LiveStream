package olmo.wellness.android.ui.screen.video_small

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo

const val LIVE_STREAM_INFO_BR_KEY = "LIVE_DATA_INFO_BR_KEY"

fun pipControllerBR(
    onClosePip: (LiveSteamShortInfo?) -> Unit,
    onRequestOpenContent: (LiveSteamShortInfo?) -> Unit,
) = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent.onIntentReceivedForPipController(
            onClosePip = onClosePip,
            onRequestOpenContent = onRequestOpenContent
        )
    }
}

fun Intent?.onIntentReceivedForPipController(
    onClosePip: (LiveSteamShortInfo?) -> Unit,
    onRequestOpenContent: (LiveSteamShortInfo?) -> Unit,
){
    if (this == null || this.action != ACTION_PIP_CONTROL) {
        return
    }

    val livestreamInfo = this.extras?.getSerializable(LIVE_STREAM_INFO_BR_KEY) as? LiveSteamShortInfo
    when(this.getIntExtra(ACTION_PIP_CONTROL, 0)){
        ACTION_CLOSE_PIP -> {
            onClosePip.invoke(livestreamInfo)
        }
        ACTION_REQUEST_OPEN_CONTENT -> {
            onRequestOpenContent.invoke(livestreamInfo)
        }
    }
}