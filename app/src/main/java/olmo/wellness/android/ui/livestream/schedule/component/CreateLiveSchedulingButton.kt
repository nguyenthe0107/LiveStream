package olmo.wellness.android.ui.livestream.schedule.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import olmo.wellness.android.R
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton

@Composable
fun CreateLiveSchedulingButton(
    modifier: Modifier,
    onClick : () -> Unit,
    title : String?= null
) {
    PrimaryLiveButton(
        text = (if (title.isNullOrBlank()){
            stringResource(id = R.string.btn_create_live_scheduling)
        }else {title}),
        modifier = modifier
            .padding(
                horizontal = 16.dp
            )
            .padding(
                bottom = 24.dp
            ), enable = true,
        onClickFunc = {
            onClick.invoke()
        }

    )
}
