package olmo.wellness.android.ui.screen.playback_video.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SurroundSound
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.screen.playback_video.explore.OlboardHomeInteractionEvents

@Composable
fun VoiceOverLayUI(
    livestreamInfo: LiveSteamShortInfo,
    tiktokInteractionEvents: ((OlboardHomeInteractionEvents) -> Unit)?=null,
    modifier: Modifier = Modifier){
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 8.dp, end = 16.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End){
        Icon(imageVector = Icons.Filled.SurroundSound,
            tint = Color.Black,
            contentDescription = null,
            modifier = Modifier.noRippleClickable {
                tiktokInteractionEvents?.invoke(OlboardHomeInteractionEvents.MuteSound)
            }
        )
    }
}