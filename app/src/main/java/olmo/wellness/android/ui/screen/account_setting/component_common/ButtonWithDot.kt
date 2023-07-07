package olmo.wellness.android.ui.screen.account_setting.component_common

import android.icu.text.CaseMap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R
import olmo.wellness.android.ui.common.dashedBorder
import olmo.wellness.android.ui.theme.Color_BLUE_7F4
import olmo.wellness.android.ui.theme.Transparent

@Composable
fun ButtonWithDot(modifier: Modifier,
                  title: String = "",
                  isHideLeftIcon: Boolean ?= null,
                  onButtonClick: (() -> Unit) ?= null){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.Center
    ) {
            OutlinedButton(
                onClick = {
                    onButtonClick?.invoke()
                }, modifier = Modifier
                    .dashedBorder(
                        width = 1.dp,
                        color = Color_BLUE_7F4,
                        shape = RoundedCornerShape(50.dp),
                        10.dp, 10.dp
                    )
                    .clip(RoundedCornerShape(40.dp))
                    .height(50.dp),
                border = BorderStroke(0.dp, Color.Transparent),
                shape = RoundedCornerShape(50.dp)
            ) {
                if(isHideLeftIcon == null || isHideLeftIcon == false){
                    Icon(
                        painter = painterResource(id = R.drawable.ic_button_add_calendar),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .background(Transparent)
                    )
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.subtitle1.copy(
                        fontSize = 16.sp, color = Color_BLUE_7F4
                    )
                )
            }
    }
}