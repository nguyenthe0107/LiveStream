package olmo.wellness.android.ui.screen.playback_video.live_home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_2.scrollEnabled

@Composable
fun HomeCategoriesList() {
    LazyRow(modifier = Modifier
        .fillMaxWidth()
        .scrollEnabled(true)
        .padding(vertical = 16.dp)
    ) {
        items(10){ index ->
            CategoryItem(index)
        }
    }
}
