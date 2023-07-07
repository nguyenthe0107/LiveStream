package olmo.wellness.android.ui.screen.playback_video.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun FilterTag(text: String, modifier: Modifier) {
    val tagModifier = modifier
        .clickable(onClick = {})
        .clip(RoundedCornerShape(4.dp))
        .alpha(0.4f)
        .background(Color.Black)
        .padding(horizontal = 8.dp, vertical = 4.dp)

    Text(
        text = text,
        color = Color.White,
        modifier = tagModifier,
        style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold)
    )
}