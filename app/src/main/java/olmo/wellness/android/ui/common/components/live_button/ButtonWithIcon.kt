package olmo.wellness.android.ui.common.components.live_button

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color

@Composable
fun ButtonWithIcon(
    iconDrawable: Int = 0,
    modifier: Modifier = Modifier,
    text: String,
    paddingVertical: Dp = 14.dp,
    paddingHorizontal: Dp = 14.dp,
    borderColor: Color = Color_LiveStream_Main_Color,
    backgroundColor: Color = Color.Transparent,
    textColor: Color = Color_LiveStream_Main_Color,
    onClickFunc: (() -> Unit)? = null
) {
    ConstraintLayout(modifier = modifier
        .fillMaxWidth()
        .background(
            backgroundColor
        )
        .border(
            width = 1.dp,
            color = borderColor,
            shape = RoundedCornerShape(50.dp)
        )
        .noRippleClickable {
            onClickFunc?.invoke()
        }) {
        val (leftIcon, centerText) = createRefs()
        Image(painter = painterResource(iconDrawable),
            contentDescription = "icon-left", modifier = Modifier.constrainAs(leftIcon) {
                start.linkTo(parent.start, 16.dp)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }, contentScale = ContentScale.Crop
        )
        Row(modifier = Modifier.constrainAs(centerText) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
        }) {
            Text(
                text = text,
                style = MaterialTheme.typography.button.copy(
                    color = textColor
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
}