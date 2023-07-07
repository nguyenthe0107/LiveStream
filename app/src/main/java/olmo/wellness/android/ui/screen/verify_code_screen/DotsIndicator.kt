package olmo.wellness.android.ui.screen.verify_code_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import olmo.wellness.android.ui.theme.Color_DOT_SLIDE_ACTIVE
import olmo.wellness.android.ui.theme.Color_DOT_SLIDE_DISABLE

@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int){
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(), horizontalArrangement = Arrangement.Center){
        items(totalDots) { index ->
            if (index == selectedIndex) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color = Color_DOT_SLIDE_ACTIVE)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color = Color_DOT_SLIDE_DISABLE)
                )
            }

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}