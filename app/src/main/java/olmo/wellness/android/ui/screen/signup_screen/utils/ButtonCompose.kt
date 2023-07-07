package olmo.wellness.android.ui.screen.signup_screen.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.ui.theme.Color_Purple_FBC
import olmo.wellness.android.ui.theme.White

@Composable
fun ButtonCompose(paddingTop: Dp?=null, paddingBottom: Dp?=null,
                  titleButton: Int?, onClick: (() -> Unit)? = null, enable: Boolean? = null) {
    Button(
        onClick = { if (enable == true) onClick?.let { it() }},
        modifier = Modifier
            .padding(top = paddingTop?:0.dp, bottom = paddingBottom?:0.dp)
            .fillMaxWidth(),
        colors = if (enable == true) {
            ButtonDefaults.buttonColors(
                backgroundColor = Color.White
            )
        } else {
            ButtonDefaults.buttonColors(
                backgroundColor = White
            )
        },
        border = BorderStroke(color = Color_Purple_FBC, width = 1.dp),
        shape = RoundedCornerShape(50.dp)
    ){
        Text(
            stringResource(id = titleButton ?: 0),
            style = TextStyle(
                color = Color_Purple_FBC,
                fontWeight = FontWeight.W700,
                fontSize = 16.sp,
                lineHeight = 24.sp,
            ),
            modifier = Modifier
                .wrapContentSize()
                .padding(
                    vertical = 10.dp,
                    horizontal = 10.dp
                )
        )
    }
}
