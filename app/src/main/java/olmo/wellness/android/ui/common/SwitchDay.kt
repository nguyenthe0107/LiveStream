package olmo.wellness.android.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.theme.Color_LiveStream_Main_Color
import olmo.wellness.android.ui.theme.White

@Composable
fun SwitchDay(
    defaultMode: MutableState<Boolean>,
    changeSwitch : ()->Unit,
    text: String,
    modifier: Modifier = Modifier
) {
        Box(
            modifier = modifier
                .width(82.dp)
                .height(32.dp),
        ) {
            Image(
                painter = painterResource(id = getSwitchBackground(mode = defaultMode.value)),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .noRippleClickable {
                        defaultMode.value = !defaultMode.value
                        changeSwitch.invoke()
                    }
            )

            if (defaultMode.value) {
                Text(
                    text = text, style = MaterialTheme.typography.subtitle1.copy(
                        color = White, fontSize = 12.sp
                    ), modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                )
            } else {
                Text(
                    text = text, style = MaterialTheme.typography.subtitle1.copy(
                        color = Color_LiveStream_Main_Color, fontSize = 12.sp
                    ), modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                )
            }
    }

}

fun getSwitchBackground(mode: Boolean): Int {
    return if (mode) {
        R.drawable.ic_switch_on
    } else {
        R.drawable.ic_switch_off
    }
}