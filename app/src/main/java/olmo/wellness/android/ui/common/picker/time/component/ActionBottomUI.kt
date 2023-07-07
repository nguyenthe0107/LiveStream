package olmo.wellness.android.ui.common.picker.time.component

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.theme.White

@Composable
fun ActionBottomUI(
    context: Context,
    onDismiss: () -> Unit,
    enableConfirm: Boolean,
    onConfirm: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .background(White)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SecondLiveButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp)
                    .noRippleClickable {
                        (context as Activity).onBackPressed()
                    },
                stringResource(R.string.cancel),
                onClickFunc = {
                    onDismiss.invoke()
                }
            )
            PrimaryLiveButton(
                enable = enableConfirm,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 6.dp),
                text = stringResource(R.string.lb_confirm),
                onClickFunc = {
                    onConfirm.invoke()
                }
            )
        }
    }
}