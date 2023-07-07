package olmo.wellness.android.ui.screen.profile_dashboard.component_common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.theme.Color_gray_FF7
import olmo.wellness.android.ui.theme.White

@Composable
fun GroupButtonBottomCompose(
    cancelCallback: (() -> Unit)? = null,
    confirmCallback: (() -> Unit)? = null,
    enable: Boolean = true,
    contentConfirm: String? = null,
    contentCancel: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(White)
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
                .noRippleClickable {
                },
            contentCancel ?: stringResource(id = R.string.cancel),
            onClickFunc = {
                cancelCallback?.invoke()
            }
        )
        PrimaryLiveButton(
            enable = enable,
            modifier = Modifier
                .weight(1f)
                .padding(start = 6.dp),
            text = contentConfirm ?: stringResource(id = R.string.lb_confirm),
            onClickFunc = {
                confirmCallback?.invoke()
            }
        )
    }
}