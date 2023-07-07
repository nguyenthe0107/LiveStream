package olmo.wellness.android.ui.screen.playback_video.common

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo

@Composable
fun VideoOverLayUI(
    livestreamInfo: LiveSteamShortInfo,
    showDescriptionVideo : Boolean ?= false,
    modifier: Modifier = Modifier){
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalAlignment = Alignment.Bottom){
        VideoInfoSection(
            Modifier.weight(1f),
            livestreamInfo,
            showDescriptionVideo
        )
    }
}