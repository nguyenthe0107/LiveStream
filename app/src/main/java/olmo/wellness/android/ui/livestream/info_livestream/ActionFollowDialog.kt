package olmo.wellness.android.ui.livestream.info_livestream

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import olmo.wellness.android.R
import olmo.wellness.android.data.model.chat.room.User
import olmo.wellness.android.extension.getNameUserChat
import olmo.wellness.android.extension.subName
import olmo.wellness.android.ui.common.avatar.UserAvatar
import olmo.wellness.android.ui.screen.profile_dashboard.component_common.GroupButtonBottomCompose
import olmo.wellness.android.ui.screen.signup_screen.utils.SpaceCompose
import olmo.wellness.android.ui.theme.Neutral_Gray_9
import olmo.wellness.android.ui.theme.White

@Composable
fun ActionFollowDialog(
    user: User? = null,
    showDialog: Boolean,
    onConfirm: ((User?) -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
) {
    if (showDialog) {
        UIDialog(user, onConfirm, onCancel)
    }
}

@Composable
private fun UIDialog(
    userInput: User?,
    onConfirm: ((User?) -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
) {
    val user = remember {
        mutableStateOf(userInput)
    }
    Dialog(onDismissRequest = { onCancel?.invoke() }) {
        Column(
            modifier = Modifier.background(
                color = White,
                shape = RoundedCornerShape(16.dp)
            )
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                SpaceCompose(height = 16.dp)
                Text(
                    text = stringResource(id = R.string.title_dialog_confirm),
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        lineHeight = 26.sp,
                        color = Neutral_Gray_9
                    )
                )
                SpaceCompose(height = 8.dp)
                Text(
                    text = stringResource(R.string.title_des_unfollow),
                    style = MaterialTheme.typography.subtitle1.copy(
                        color = Neutral_Gray_9,
                        textAlign = TextAlign.Center
                    )
                )
                SpaceCompose(height = 4.dp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    UserAvatar(
                        name = getNameUserChat(user.value),
                        urlAvatar = if (!user.value?.avatar.isNullOrBlank()) {
                            user.value?.avatar
                        } else {
                            ""
                        },
                        modifier = Modifier
                            .padding(horizontal = 7.dp)
                            .size(25.dp)
                    )
                    Text(
                        text = getNameUserChat(user.value).subName(),
                        style = MaterialTheme.typography.subtitle2.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Neutral_Gray_9
                        )
                    )
                }

                Spacer(
                    modifier = Modifier
                        .background(color = White.copy(alpha = 0.9f))
                        .fillMaxWidth()
                        .height(1.dp)
                )
            }

            GroupButtonBottomCompose(confirmCallback = {
                onConfirm?.invoke(user.value)
            }, cancelCallback = {
                onCancel?.invoke()
            },
            contentConfirm = stringResource(id = R.string.action_unfollow ),
            contentCancel = stringResource(id = R.string.action_keep_follow)
            )
        }
    }
}