package olmo.wellness.android.ui.common.components.live_button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.extension.hideKeyboard
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.screen.MainActivity
import olmo.wellness.android.ui.theme.*

@Composable
fun PrimaryLiveButton(
    modifier: Modifier = Modifier,
    text: String,
    paddingVertical: Dp = 12.dp,
    paddingHorizontal: Dp = 12.dp,
    isWrapContentWidth: Boolean = false,
    backgroundColor: Color? = null,
    textColor: Color = White,
    enable : Boolean = true,
    onClickFunc : (() -> Unit) ?= null
) {
    val focusManager = LocalFocusManager.current
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .then(if (isWrapContentWidth) Modifier.wrapContentWidth() else Modifier.fillMaxWidth())
            .then(
                if (backgroundColor != null)
                    Modifier
                        .background(
                            backgroundColor,
                            shape = RoundedCornerShape(50.dp)
                        )
            else
                Modifier.background(
                        brush =  if (enable){ Brush.horizontalGradient(
                            listOf(
                                Color_LiveStream_Main_Color,
                                Color_LiveStream_Main_Color,
                                Color_LiveStream_Light_Color,
                            )
                        ) } else {  Brush.horizontalGradient(
                            listOf(
                                Neutral_Gray_4,
                                Neutral_Gray_4,
                                Neutral_Gray_4,
                            )
                        ) },
                        shape = RoundedCornerShape(50.dp)
                    )
            )
            .noRippleClickable {
                if (enable) {
                    focusManager.clearFocus()
                    onClickFunc?.invoke()
                }
            }
    ){
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
                .padding(
                    vertical = paddingVertical,
                    horizontal = paddingHorizontal
                )
        )
    }
}