package olmo.wellness.android.ui.common.components.dialog_confirm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import olmo.wellness.android.R
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.components.live_button.PrimaryLiveButton
import olmo.wellness.android.ui.common.components.live_button.SecondLiveButton
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.*

@Composable
fun DialogConfirmWithIconLiveStream(
    openDialogCustom: MutableState<Boolean>,
    confirmCallback: ((Boolean) -> Unit)? = null,
    cancelCallback: ((Boolean) -> Unit)? = null,
    content: String= ""
){
    if (openDialogCustom.value) {
        Dialog(onDismissRequest = { openDialogCustom.value = false }) {
            ConfirmedRemovedLiveStream(content = content, cancelCallback = {
                cancelCallback?.invoke(it)
            }, confirmCallback = {
                confirmCallback?.invoke(it)
            })
        }
    }
}

@Composable
fun ConfirmedRemovedLiveStream(
    content: String,
    confirmCallback: ((Boolean) -> Unit)? = null,
    cancelCallback: ((Boolean) -> Unit)? = null
){
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
                .background(White), horizontalAlignment = Alignment.CenterHorizontally){
            SpaceCompose(height = 10.dp)
            AsyncImage(model = R.drawable.olmo_img_onboard_right, contentDescription = "image_right", modifier = Modifier.size(94.dp, 112.dp))
            SpaceCompose(height = 20.dp)
            Text(
                text = content,
                style = MaterialTheme.typography.body1.copy(
                    fontSize = 18.sp,
                    lineHeight = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Neutral_Gray_9
                ),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            SpaceCompose(height = 20.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()){
                PrimaryLiveButton(
                    enable = true,
                    modifier = Modifier.padding(),
                    text = stringResource(R.string.action_confirm_dialog_close),
                    onClickFunc = {
                        confirmCallback?.invoke(true)
                    }
                )
            }
            SpaceCompose(height = 20.dp)
        }
    }
}