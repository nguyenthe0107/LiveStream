package olmo.wellness.android.ui.common.components.live_button

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.theme.Color_LiveStream_Light_Color
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color
import olmo.wellness.android.ui.theme.Transparent
import olmo.wellness.android.ui.theme.White

@Composable
fun ButtonIcon(
    title: String,
    iconLeft: Int?=null,
    iconRight: Int?=null,
    backgroundColor: Color = White,
    radius: Dp = 8.dp,
    paddingVertical: Dp = 12.dp,
    paddingHorizontal: Dp = 12.dp,
    borderColor: Color = Color_LiveStream_Main_Color,
    textColor: Color = Color_LiveStream_Main_Color,
    modifier: Modifier = Modifier,
    onClickFunc: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(radius)
            )
            .noRippleClickable {
                focusManager.clearFocus()
                onClickFunc.invoke()
            }
            .padding(
                vertical = paddingVertical,
                horizontal = paddingHorizontal
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (iconLeft != null) {
            Image(
                painter = painterResource(iconLeft), contentDescription = "Icon-left",
                modifier = Modifier.padding(end = 4.dp),
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle2.copy(
                color = textColor,
                lineHeight = 8.sp,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            ),
            modifier = Modifier.weight(1f)
        )


        if (iconRight != null) {
            Image(
                painter = painterResource(iconRight), contentDescription = "Icon-Right",
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}