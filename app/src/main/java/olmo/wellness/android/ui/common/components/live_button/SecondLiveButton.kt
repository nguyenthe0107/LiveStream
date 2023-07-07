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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.extension.hideKeyboard
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.theme.*

@Composable
fun SecondLiveButton(
    modifier: Modifier = Modifier,
    text: String,
    radius : Dp= 50.dp,
    paddingVertical: Dp = 12.dp,
    paddingHorizontal: Dp = 12.dp,
    borderColor: Color = Color_LiveStream_Main_Color,
    backgroundColor: Color = Color.Transparent,
    textColor: Color = Color_LiveStream_Main_Color,
    alignText: Alignment=Alignment.Center,
    iconLeft: Int? = null,
    iconRight : Int?=null,
    enable : Boolean? = null,
    onClickFunc: (() -> Unit)? = null
) {
    val focusManager = LocalFocusManager.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = if (enable == true) {
                    Brush.horizontalGradient(
                        listOf(
                            Color_LiveStream_Main_Color,
                            Color_LiveStream_Main_Color,
                            Color_LiveStream_Light_Color,
                        )
                    )
                } else {
                    Brush.horizontalGradient(
                        listOf(
                            Transparent,
                            Transparent,
                            Transparent,
                        )
                    )
                },
                shape = RoundedCornerShape(radius),
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(radius)
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
        Text(
            text = text,
            style = MaterialTheme.typography.subtitle2.copy(
                color = if(enable == true) White else textColor,
                lineHeight = 24.sp,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            ),
            modifier = Modifier
                .wrapContentSize()
                .align(alignText)
        )
        if (iconLeft != null) {
            Image(
                painter = painterResource(iconLeft), contentDescription = "Icon-left",
                modifier = Modifier.align(Alignment.CenterStart),
            )
        }

        if (iconRight!=null){
            Image(
                painter = painterResource(iconRight), contentDescription = "Icon-Right",
                modifier = Modifier.align(Alignment.CenterEnd),
                colorFilter = if(enable == true){
                    ColorFilter.tint(White)
                }else {
                    ColorFilter.tint(Color.Unspecified)
                }
            )
        }
    }
}