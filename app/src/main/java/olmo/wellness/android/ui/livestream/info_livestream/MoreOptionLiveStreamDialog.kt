package olmo.wellness.android.ui.livestream.info_livestream

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import olmo.wellness.android.R
import olmo.wellness.android.data.model.chat.ChatMessage
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import olmo.wellness.android.ui.theme.Neutral_Gray_4
import olmo.wellness.android.ui.theme.Neutral_Gray_9
import olmo.wellness.android.ui.theme.White

@Composable
fun MoreOptionLiveStreamDialog(
    livestreamInfo: LiveSteamShortInfo?= null,
    msg: ChatMessage?,
    onShareOnProfile : ((LiveSteamShortInfo?) -> Unit)? = null,
    onShareOnSocialMedia : ((LiveSteamShortInfo?) -> Unit)? = null,
    onSendMessageToHost : ((LiveSteamShortInfo?) -> Unit)? = null,
    onReportAdmin : ((LiveSteamShortInfo?) -> Unit)? = null,
    showDialog: Boolean,
    onCancel: (() -> Unit)? = null,
) {
    if (showDialog) {
        UIDialog(livestreamInfo,msg,onShareOnProfile, onShareOnSocialMedia, onSendMessageToHost,onReportAdmin,onCancel)
    }
}

@Composable
private fun UIDialog(
    livestreamInfo : LiveSteamShortInfo ?= null,
    msg: ChatMessage?,
    onShareOnProfile : ((LiveSteamShortInfo?) -> Unit)? = null,
    onShareOnSocialMedia : ((LiveSteamShortInfo?) -> Unit)? = null,
    onSendMessageToHost : ((LiveSteamShortInfo?) -> Unit)? = null,
    onReportAdmin : ((LiveSteamShortInfo?) -> Unit)? = null,
    onCancel: (() -> Unit)?,
) {
//    val user = createUser(
//        id = msg?.userId,
//        name = msg?.userName,
//        email = msg?.userEmail,
//        avatar = msg?.userAvatar
//    )
//    val idMessage = msg?.id
    Dialog(onDismissRequest = { onCancel?.invoke() }) {
        Column(modifier = Modifier){
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
                            onShareOnProfile?.invoke(livestreamInfo)
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.text_share_profile),
                        style = MaterialTheme.typography.subtitle1.copy(
                            color = Neutral_Gray_9,
                        )
                    )
                }

                Spacer(
                    modifier = Modifier
                        .background(Neutral_Gray_4)
                        .fillMaxWidth()
                        .height(1.dp)
                )

                /*Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable {
                            onShareOnSocialMedia?.invoke(livestreamInfo)
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.text_share_social_profile),
                        style = MaterialTheme.typography.subtitle1.copy(
                            color = Neutral_Gray_9
                        )
                    )
                }*/

                Spacer(
                    modifier = Modifier
                        .background(Neutral_Gray_4)
                        .fillMaxWidth()
                        .height(1.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable {
                            onSendMessageToHost?.invoke(livestreamInfo)
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.text_send_message_to_host),
                        style = MaterialTheme.typography.subtitle1.copy(
                            color = Neutral_Gray_9
                        )
                    )
                }

                Spacer(
                    modifier = Modifier
                        .background(Neutral_Gray_4)
                        .fillMaxWidth()
                        .height(1.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable {
                            onReportAdmin?.invoke(livestreamInfo)
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.text_report_to_admin),
                        style = MaterialTheme.typography.subtitle1.copy(
                            color = Neutral_Gray_9
                        )
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
                    style = MaterialTheme.typography.subtitle1.copy(
                        color = Neutral_Gray_9
                    )
                )
            }

        }
    }
}