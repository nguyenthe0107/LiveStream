package olmo.wellness.android.ui.livestream.schedule.component

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceHorizontalCompose

@Composable
fun DeleteItemSwipeUpcoming(modifier : Modifier, callbackDeleteItemUpcoming: (() -> Unit) ?= null){
    Row(
        modifier.noRippleClickable {
            callbackDeleteItemUpcoming?.invoke()
        },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(
            onClick = {
                callbackDeleteItemUpcoming?.invoke()
            },
            modifier = Modifier
                .size(25.dp)){
            Icon(
                painter = painterResource(id = R.drawable.olmo_ic_trash_circle),
                contentDescription = "Delete",
                tint = Color.Unspecified
            )
        }
        SpaceHorizontalCompose(width = 10.dp)
        Text(
            text = "Delete", style = MaterialTheme.typography.subtitle2.copy(
                color = Color.White,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Normal
            )
        )
    }
}