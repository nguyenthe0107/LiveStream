package olmo.wellness.android.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import olmo.wellness.android.ui.theme.Color_Purple_FBC
import olmo.wellness.android.ui.theme.Color_gray_FF7
import olmo.wellness.android.ui.theme.White

@Composable
fun TextViewDrawable(
    text: String,
    @DrawableRes drawableRes: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                vertical = 14.dp,
                horizontal = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text,
            style = MaterialTheme.typography.subtitle2,
            color = Color_Purple_FBC
        )
        Image(
            painter = painterResource(id = drawableRes),
            contentDescription = null,
            modifier = Modifier
                .width(20.dp)
                .height(20.dp)
        )
    }
}