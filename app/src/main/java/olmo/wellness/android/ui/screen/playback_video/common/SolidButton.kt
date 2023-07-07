package olmo.wellness.android.ui.screen.playback_video.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import olmo.wellness.android.ui.theme.Color_Green_Main
import olmo.wellness.android.ui.theme.White

@Composable
fun SolidButton(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color_Green_Main,
    horizontalPadding: Dp = 0.dp,
    verticalPadding: Dp = 0.dp,
    cornerRadius: Dp = 8.dp
) {
    Box(modifier = modifier
        .wrapContentHeight()
        .wrapContentWidth()
        .background(
            backgroundColor,
            shape = RoundedCornerShape(cornerRadius)
        )){
        Text(
            text = text,
            Modifier.padding(
                horizontal = horizontalPadding,
                vertical = verticalPadding
            ),
            style = TextStyle(
                color = White
            )
        )
    }
}