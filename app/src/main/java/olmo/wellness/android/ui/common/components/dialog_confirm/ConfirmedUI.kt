package olmo.wellness.android.ui.common.components.dialog_confirm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.*

@Composable
fun ConfirmedUI(
    confirmCallback: ((Boolean) -> Unit)? = null,
    cancelCallback: ((Boolean) -> Unit)? = null
) {
    Card(
        shape = RoundedCornerShape(marginStandard),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        elevation = defaultShadow
    ) {
        Column(
            Modifier
                .padding(horizontal = marginDouble)
                .fillMaxWidth()
                .background(White)
        ){
            Text(
                text = stringResource(id = R.string.title_dialog_confirm),
                color = Neutral_Gray_9,
                style = MaterialTheme.typography.body1.copy(
                    fontSize = 18.sp,
                    lineHeight = 26.sp,
                    fontWeight = FontWeight.Bold
                ),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 10.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = stringResource(id = R.string.content_dialog_confirm),
                color = Neutral_Gray_9,
                style = MaterialTheme.typography.subtitle2.copy(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal
                ),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 14.dp)
                    .align(Alignment.CenterHorizontally)
            )
            SpaceCompose(height = 20.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()){
                SecondLiveButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 6.dp)
                        .noRippleClickable {},
                    stringResource(R.string.cancel),
                    onClickFunc = {
                        cancelCallback?.invoke(true)
                    },
                    backgroundColor = White
                )
                PrimaryLiveButton(
                    enable = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 6.dp),
                    text = stringResource(R.string.action_confirm_dialog_confirm),
                    onClickFunc = {
                        confirmCallback?.invoke(true)
                    }
                )
            }
            SpaceCompose(height = 20.dp)
        }
    }
}