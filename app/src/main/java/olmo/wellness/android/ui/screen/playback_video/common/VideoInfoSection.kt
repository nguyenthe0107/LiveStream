package olmo.wellness.android.ui.screen.playback_video.common

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo

@Composable
fun VideoInfoSection(modifier: Modifier,
                     liveStreamInfo: LiveSteamShortInfo,
                     showDescriptionVideo: Boolean ?= false) {
    Column(modifier = modifier.padding(16.dp)) {
        Row(
            modifier = Modifier
                .background(color = Color.Transparent)
                .fillMaxWidth()
        ){
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(end = 38.dp)
            ){
                if(showDescriptionVideo == true){
                    Text(
                        text = liveStreamInfo.description.orEmpty(),
                        style = MaterialTheme.typography.caption.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Start,
                            lineHeight = 16.sp
                        ),
                    )
                }
            }
        }
    }
}