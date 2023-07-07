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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.extension.hideKeyboard
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceHorizontalCompose
import olmo.wellness.android.ui.theme.Color_Green_Main
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color
import olmo.wellness.android.ui.theme.White

@Composable
fun SecondLiveButtonWithIcon(
    modifier: Modifier = Modifier,
    text: String,
    paddingVertical: Dp = 12.dp,
    paddingHorizontal: Dp = 12.dp,
    borderColor: Color = Color_LiveStream_Main_Color,
    backgroundColor: Color = Color.Transparent,
    textColor: Color = Color_LiveStream_Main_Color,
    iconLeft: Int? = null,
    iconRight: Int? = null,
    isVisibleIcon: Boolean ?= false,
    onClickFunc: (() -> Unit)? = null
) {
    val focusManager = LocalFocusManager.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                backgroundColor,
                shape = RoundedCornerShape(50.dp)
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(50.dp)
            )
            .noRippleClickable {
                focusManager.clearFocus()
                onClickFunc?.invoke()
            }
            .padding(
                vertical = paddingVertical,
                horizontal = paddingHorizontal
            )
    ) {
        Row(Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            if (iconLeft != null) {
                Image(
                    painter = painterResource(iconLeft), contentDescription = "Icon-left",
                    modifier = Modifier
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.subtitle2.copy(
                    color = textColor,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .wrapContentSize()
            )
            if (iconRight != null && isVisibleIcon == true) {
                SpaceHorizontalCompose(width = 10.dp)
                Image(
                    painter = painterResource(iconRight), contentDescription = "Icon-Right",
                )
            }
        }
    }
}