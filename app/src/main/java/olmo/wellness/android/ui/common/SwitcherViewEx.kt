package olmo.wellness.android.ui.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.theme.Color_Purple_FBC
import olmo.wellness.android.ui.theme.Neutral_Gray_9
import olmo.wellness.android.ui.theme.White

@Composable
fun SwitchViewEx(
    modifier: Modifier = Modifier,
    text: String,
    defaultMode: Boolean = false,
    icOn: Int = R.drawable.img_switch_on,
    icOff: Int = R.drawable.img_switch_off_white,
    controller: ((MutableState<Boolean>) -> Unit)? = null,
    onSwitchChanged: (isOn: Boolean) -> Unit,
) {
    val isModeOn = remember {
        mutableStateOf(defaultMode)
    }

    controller?.invoke(
        isModeOn
    )

    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                White
            )
            .padding(
                vertical = 14.dp,
                horizontal = 16.dp
            ),
    ) {
        val (textView, switch) = createRefs()

        Text(
            text,
            style = MaterialTheme.typography.body2,
            color = Neutral_Gray_9,
            modifier = Modifier
                .wrapContentHeight()
                .constrainAs(textView) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(switch.start)
                    width = Dimension.fillToConstraints
                },
        )
        Image(
            painter = painterResource(id = if (isModeOn.value) icOn else icOff),
            contentDescription = null,
            modifier = Modifier
                .width(44.dp)
                .height(22.dp)
                .defaultMinSize(minWidth = 44.dp, minHeight = 22.dp)
                .noRippleClickable {
                    isModeOn.value = !isModeOn.value
                    onSwitchChanged.invoke(isModeOn.value)
                }
                .constrainAs(switch) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
        )
    }
}

@Composable
fun SwitchOnOff(
    scale: Float = 1.5f,
    width: Dp = 30.dp,
    height: Dp = 16.dp,
    switchOn: Boolean,
    switchChange: (Boolean) -> Unit,
    strokeWidth: Dp = 1.dp,
    checkedTrackColor: Color = Color_Purple_FBC,
    uncheckedTrackColor: Color = White,
    gapBetweenThumbAndTrackEdge: Dp = 2.dp
) {

    val thumbRadius = (height / 2) - gapBetweenThumbAndTrackEdge
    val animatePosition = animateFloatAsState(
        targetValue = if (switchOn)
            with(LocalDensity.current) { (width - thumbRadius - gapBetweenThumbAndTrackEdge).toPx() }
        else
            with(LocalDensity.current) { (thumbRadius + gapBetweenThumbAndTrackEdge).toPx() }
    )

    Canvas(
        modifier = Modifier
            .size(width = width, height = height)
            .scale(scale = scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                    }
                )
            }
            .noRippleClickable {
                switchChange.invoke(!switchOn)
            }
    ) {

        drawRoundRect(
            color = if (switchOn) checkedTrackColor else uncheckedTrackColor,
            cornerRadius = CornerRadius(x = 10.dp.toPx(), y = 10.dp.toPx()),
        )

        // Track
        drawRoundRect(
            color = checkedTrackColor,
            cornerRadius = CornerRadius(x = 10.dp.toPx(), y = 10.dp.toPx()),
            style = Stroke(width = strokeWidth.toPx())
        )

        // Thumb
        drawCircle(
            color = if (switchOn) uncheckedTrackColor else checkedTrackColor,
            radius = thumbRadius.toPx(),
            center = Offset(
                x = animatePosition.value,
                y = size.height / 2
            )
        )
    }
}


@Composable
fun ItemSwitch(
    title: String,
    modifier: Modifier = Modifier,
    switchDefault: Boolean,
    onSwitch: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                vertical = 20.dp,
                horizontal = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle1.copy(
                color = Color_Purple_FBC,
                fontSize = 14.sp
            ),
            modifier = Modifier.weight(1f)
        )

        SwitchOnOff(switchOn = switchDefault, switchChange = { result ->
            onSwitch.invoke(result)
        })

    }
}
