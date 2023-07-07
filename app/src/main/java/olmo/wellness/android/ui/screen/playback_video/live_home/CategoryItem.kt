package olmo.wellness.android.ui.screen.playback_video.live_home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import olmo.wellness.android.ui.theme.randomColor

@Composable
fun CategoryItem(index: Int) {
    Box(modifier = Modifier
        .padding(
            start = 16.dp,
            end = 8.dp
        )
        .size(54.dp)
        .background(
            color = randomColor(),
            shape = RoundedCornerShape(54.dp)
        )
    )
}