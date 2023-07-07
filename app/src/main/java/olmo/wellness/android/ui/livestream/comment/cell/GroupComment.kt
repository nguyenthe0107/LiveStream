package olmo.wellness.android.ui.livestream.comment.cell

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.R
import olmo.wellness.android.data.model.chat.ChatMessage
import olmo.wellness.android.data.model.chat.MessageType
import olmo.wellness.android.extension.getNameUserChat
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.common.avatar.UserAvatar
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.screen.playback_video.on_board.temp
import olmo.wellness.android.ui.theme.Error_500
import olmo.wellness.android.ui.theme.Neutral_Gray_7
import olmo.wellness.android.ui.theme.Neutral_Gray_9

@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@Composable
fun GroupComment(
    message: ChatMessage?,
    isRoot: Boolean = true,
    onReaction: (ChatMessage?) -> Unit,
    onReply: ((ChatMessage?) -> Unit)? = null
) {

    val listReplies = remember {
        mutableStateOf<MutableList<ChatMessage?>>(mutableListOf())
    }

    if (listReplies.value.isNotEmpty() && message?.listChild?.isNotEmpty() == true && listReplies.value[0]?.id != message.listChild?.get(0)?.id) {
        val temp = listReplies.value.map { msg -> msg }.toMutableList()
        temp.add(0, message.listChild?.get(0))
        listReplies.value = temp
    }

    message?.listChild?.let {
        if (it.size<=3){
            listReplies.value= message.listChild!!
        }
    }
    if (message?.listChild==null || message?.listChild?.isEmpty()==true){
        listReplies.value= mutableListOf()

    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        UserAvatar(
            name = getNameUserChat(message?.user),
            urlAvatar = message?.user?.avatar ?: "",
            modifier = Modifier
                .size(32.dp)
        )

        Column(modifier = Modifier.padding(start = 5.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = getNameUserChat(message?.user),
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontSize = 12.sp, lineHeight = 18.sp
                    ),
                    modifier = Modifier, maxLines = 1,
                )

                Text(
                    text = DateTimeHelper.convertToStringHour(message?.createdAtTimestamp),
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .weight(1f),
                    style = MaterialTheme.typography.subtitle1.copy(
                        fontSize = 10.sp,
                        lineHeight = 14.sp,
                        color = Neutral_Gray_7
                    )
                )
                message?.listChild?.size?.let { sizeChild ->
                    if (sizeChild > 3) {
                        ViewReply(
                            modifier = Modifier,
                            count = sizeChild - listReplies.value.size,
                            showMore = {
                                if (it) {
                                    if (listReplies.value.size < sizeChild) {
                                        val temp =
                                            listReplies.value.map { msg -> msg }.toMutableList()
                                        var position = listReplies.value.size
                                        val max = position + 5
                                        while (position < sizeChild && position < max) {
                                            temp.add(message.listChild?.get(position))
                                            position++
                                        }
                                        listReplies.value = temp
                                    }
                                } else {
                                    listReplies.value = mutableListOf()
                                }
                            })
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val content= if (message?.type== MessageType.TEXT.value){
                    message.content
                }else{
                    message?.objectData?.name?:""
                }
                    Text(
                        text = content?:"",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    )

                Row(modifier = Modifier) {
                    if (isRoot) {
                        Icon(painter = painterResource(id = R.drawable.ic_comment_reply),
                            contentDescription = "Reply",
                            modifier = Modifier
                                .size(15.dp)
                                .noRippleClickable {
                                    onReply?.invoke(message)
                                })
                    }

                    Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                    val color =
                        (if (!message?.reactions.isNullOrEmpty()) Error_500 else Neutral_Gray_9)
                    val icon =
                        (if (!message?.reactions.isNullOrEmpty()) R.drawable.ic_like_comment else R.drawable.ic_heart)
                    Icon(painter = painterResource(id = icon),
                        contentDescription = "Heart",
                        tint = color,
                        modifier = Modifier
                            .size(15.dp)
                            .noRippleClickable {
                                if (message?.reactions.isNullOrEmpty()) {
                                    onReaction.invoke(message)
                                }
                            })
                }
            }

            listReplies.value.forEach { reply ->
                GroupComment(message = reply, isRoot = false, onReaction = {
                    onReaction.invoke(it)
                })
            }
        }
    }
}


@Composable
fun ViewReply(modifier: Modifier, count: Int, showMore: (Boolean) -> Unit) {
    if (count > 0) {
        Row(
            modifier = modifier.noRippleClickable {
                showMore.invoke(true)
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${stringResource(id = R.string.lb_view_reply)} (${count})",
                modifier = Modifier.padding(end = 5.dp),
                style = MaterialTheme.typography.subtitle2.copy(
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = Neutral_Gray_7
                )
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_up),
                contentDescription = "Down",
                tint = Neutral_Gray_9,
                modifier = Modifier
                    .rotate(180f)
                    .size(15.dp)
            )
        }
    } else {
        Row(modifier = modifier.noRippleClickable {
            showMore.invoke(false)
        }) {
            Text(
                text = stringResource(R.string.lb_hide),
                modifier = Modifier.padding(end = 5.dp),
                style = MaterialTheme.typography.subtitle2.copy(
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = Neutral_Gray_7
                )
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_up),
                contentDescription = "Up",
                modifier = Modifier.size(15.dp),
                tint = Neutral_Gray_9,
            )
        }
    }
}

