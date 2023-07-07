package olmo.wellness.android.ui.livestream.info_livestream

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import olmo.wellness.android.R
import olmo.wellness.android.data.model.chat.ChatMessage
import olmo.wellness.android.data.model.chat.room.User
import olmo.wellness.android.extension.getNameUserChat
import olmo.wellness.android.extension.subName
import olmo.wellness.android.ui.common.avatar.UserAvatar
import olmo.wellness.android.ui.theme.White

@Composable
fun ActionMessageDialog(
    msg: ChatMessage?,
    showDialog: Boolean,
    onReply: ((User?) -> Unit)? = null,
    onLikeComment: ((User?, String?) -> Unit)? = null,
    onCancel: (() -> Unit)? = null
) {
    if (showDialog) {
        UIDialog(msg, onCancel, onReply, onLikeComment)
    }
}

@Composable
private fun UIDialog(
    msg: ChatMessage?,
    onCancel: (() -> Unit)?,
    onReply: ((User?) -> Unit)?,
    onLikeComment: ((User?, String?) -> Unit)?
) {
    val idMessage = remember {
        mutableStateOf(msg?.id)
    }
    val user = remember {
        mutableStateOf(msg?.user)
    }
    Dialog(onDismissRequest = { onCancel?.invoke() }) {
        Column(
            modifier = Modifier
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = White,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable {
                            onReply?.invoke(user.value)
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.lb_reply_to),
                        style = MaterialTheme.typography.subtitle1
                    )
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
                        style = MaterialTheme.typography.subtitle2
                    )
                }

                Spacer(
                    modifier = Modifier
                        .background(color = White.copy(alpha = 0.9f))
                        .fillMaxWidth()
                        .height(1.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable {
                            msg?.user?.let { onLikeComment?.invoke(it, idMessage.value) }
                            onLikeComment?.invoke(user.value,idMessage.value)
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.lb_like_this_comment),
                        style = MaterialTheme.typography.subtitle1
                    )
                }
            }


            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .background(
                        color = White.copy(
                            alpha = 0.7f
                        ), shape = RoundedCornerShape(16.dp)
                    )
                    .height(50.dp)
                    .clickable {
                        onCancel?.invoke()
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    style = MaterialTheme.typography.subtitle1
                )
            }

        }
    }
}