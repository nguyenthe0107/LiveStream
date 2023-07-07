package olmo.wellness.android.ui.livestream.comment.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import olmo.wellness.android.data.model.chat.ChatMessage
import olmo.wellness.android.ui.livestream.chatlivestream.state.ChatLivestreamUiState
import olmo.wellness.android.ui.livestream.chatlivestream.view.ViewInputChat
import olmo.wellness.android.ui.livestream.comment.cell.GroupComment
import olmo.wellness.android.ui.theme.*

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CommentContent(
    roomChatState: ChatLivestreamUiState?,
    messageReply: MutableState<ChatMessage?>? = null,
    isInputChat: MutableState<Boolean>? = null,
    isShowAddComment: MutableState<Boolean>? = null,
    onReaction : (ChatMessage?)->Unit,
    ) {
    val scrollState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f)
    ) {
        CommentUi(
            messages = roomChatState?.messages,
            messageReply = messageReply,
            scrollState = scrollState,
            isInputChat = isInputChat,
            isShowAddComment = isShowAddComment, onReaction = onReaction
        )
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun CommentUi(
    messages: List<ChatMessage?>?,
    onReaction : (ChatMessage?)->Unit,
    messageReply: MutableState<ChatMessage?>? = null,
    scrollState: LazyListState,
    isInputChat: MutableState<Boolean>? = null,
    isShowAddComment: MutableState<Boolean>? = null,

    ) {
    Scaffold(modifier = Modifier.fillMaxWidth(),
        bottomBar = {
            if (isShowAddComment?.value == true) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    elevation = 5.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 15.dp)
                    ) {
                        ViewInputChat(modifier = Modifier.fillMaxWidth(),
                            backgroundColor = Black_037.copy(
                                alpha = 0.5f
                            ),
                            onComment = {
                                isInputChat?.value = true
                            })
                        Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    }

                }
            }
        }) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    if (messages != null && messages.isNotEmpty()) {
                        Text(
                            text = "${messages.size} comments",
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.subtitle2.copy(
                                fontSize = 20.sp, lineHeight = 28.sp,
                            )
                        )
                    } else {
                        Text(
                            text = stringResource(olmo.wellness.android.R.string.lb_no_comments),
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.subtitle2.copy(
                                fontSize = 20.sp, lineHeight = 28.sp,
                            )
                        )
                    }

                    Spacer(
                        modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(color = Neutral_Gray_3)
                            .align(Alignment.BottomStart)
                    )
                }

                if (messages != null && messages.isNotEmpty()) {
                    LazyColumn(
                        reverseLayout = false,
                        state = scrollState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 15.dp, start = 15.dp, bottom = 80.dp)
                            .weight(1f)
                    ) {
                        messages.forEachIndexed { index, message ->
                            item(key = "head $index") {
                                GroupComment(message = message, onReply = {
                                    messageReply?.value = it
                                    isInputChat?.value = true
                                }, onReaction = onReaction)

                            }
                        }
                    }
                }
            }

            if (isInputChat?.value == true) {
                Spacer(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Color_gray_4D0.copy(
                                alpha = 0.5f
                            )
                        )
                )
            }
        }

    }


}