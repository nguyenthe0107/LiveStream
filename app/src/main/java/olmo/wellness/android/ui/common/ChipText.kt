package olmo.wellness.android.ui.common

import androidx.annotation.ColorRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import olmo.wellness.android.ui.theme.Black_037
import olmo.wellness.android.ui.theme.Neutral_Gray_3

@Composable
fun ChipText(
    modifier: Modifier = Modifier,
    text: String,
    @ColorRes background: Color = Black_037,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .background(
                Neutral_Gray_3,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable {
                onClick?.invoke()
            }
    ){
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = 14.dp,
                vertical = 7.dp,
            ),
            style = MaterialTheme
                .typography
                .body2.copy(
                    color = background
                ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}