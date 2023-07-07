package olmo.wellness.android.ui.common.components.live_button

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.theme.White

@Composable
fun CoupleLiveButton(
    modifier: Modifier = Modifier,
    confirmText: String,
    cancelText: String,
    confirmCallback: (() -> Unit) ?= null,
    cancelCallback: (() -> Unit) ?= null,
) {

    Box(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(White)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    bottom = 32.dp,
                    top = 12.dp,
                    end = 16.dp,
                    start = 16.dp
                )
        ) {
            SecondLiveButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp)
                ,
                cancelText,
                onClickFunc = {
                    cancelCallback?.invoke()
                }
            )
            PrimaryLiveButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 6.dp),
                confirmText,
                onClickFunc = {
                    confirmCallback?.invoke()
                }
            )
        }
    }
}