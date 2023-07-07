package olmo.wellness.android.ui.livestream.chatlivestream.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import olmo.wellness.android.R
import olmo.wellness.android.data.model.chat.ChatMessage
import olmo.wellness.android.data.model.chat.MessageType
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.extension.OnBottomReached
import olmo.wellness.android.extension.getNameUserChat
import olmo.wellness.android.extension.noRippleClickable
import olmo.wellness.android.ui.booking.book_now_livestream.cell.BookingService
import olmo.wellness.android.ui.common.avatar.UserAvatar
import olmo.wellness.android.ui.common.messageFormatter
import olmo.wellness.android.ui.livestream.chatlivestream.state.ChatLivestreamUiState
import olmo.wellness.android.ui.screen.playback_video.onlive.getTypeDonation
import olmo.wellness.android.ui.theme.*

val WidthView = 290.dp

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ChatLivestreamContent(
    uiState: ChatLivestreamUiState?,
    modifier: Modifier = Modifier,
    isShowAddComment: Boolean,
    onComment: () -> Unit,
    onTouchMessage: (ChatMessage) -> Unit,
    isBuyer: Boolean,
    serviceBooking: ServiceBooking? = null,
    onCloseBook: (() -> Unit)? = null,
    onBookingCallback: ((ServiceBooking?) -> Unit)? = null,
    onLoadMore: () -> Unit,
) {
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Box(
        modifier = modifier
            .background(Transparent)
            .width(WidthView)
    ) {
        Column(
            Modifier
                .width(WidthView)
                .align(Alignment.BottomStart)
//                .padding(bottom = padding_10)
                .background(Transparent)
        ) {
            MessageUi(
                uiState?.messages,
                scrollState = scrollState,
                modifier = Modifier
                    .weight(1f)
                    .background(Transparent)
                    .width(WidthView),
                onTouchMessage = { msg ->
                    if (msg?.id != null) {
                        onTouchMessage.invoke(msg)
                    }
                }
            )

            if (serviceBooking != null) {
                BookingService(booking = serviceBooking,
                    isBuyer = isBuyer,
                    onCloseBook = onCloseBook,
                    onBookingCallback = onBookingCallback)
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (isShowAddComment) {
                ViewInputChat(
                    modifier = Modifier.width(WidthView),
                    backgroundColor = Neutral_Gray_12,
                    onComment = onComment
                )
            }
        }
    }

    scrollState.OnBottomReached {
        onLoadMore.invoke()
    }
}

const val ConversationTestTag = "ConversationTestTag"

@Composable
fun MessageUi(
    messages: List<ChatMessage?>?,
    scrollState: LazyListState,
    modifier: Modifier = Modifier,
    onTouchMessage: (ChatMessage?) -> Unit,
) {
    ConstraintLayout(modifier = Modifier) {
        val (viewMessages, viewBlur) = createRefs()

        LazyColumn(
            reverseLayout = true,
            state = scrollState,
            modifier = Modifier
                .height(180.dp)
                .width(WidthView)
                .testTag(ConversationTestTag)
                .constrainAs(viewMessages) {
                    bottom.linkTo(viewBlur.top)
                    start.linkTo(parent.start)
                }
        ) {
            if (!messages.isNullOrEmpty()) {
                for (index in messages.indices) {
                    val content = messages[index]
                    item {
                        Column(modifier = Modifier) {
                            if (index == messages.size - 1) {
                                Spacer(modifier = Modifier.padding(vertical = 50.dp))
                            }
                            Message(content, onTouchMessage)

                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier
            .padding(10.dp)
            .constrainAs(viewBlur) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
            })

    }
}

@Composable
fun Message(msg: ChatMessage?, onTouchMessage: (ChatMessage?) -> Unit) {
    Row(
        modifier = Modifier
            .padding(top = 10.dp)
            .noRippleClickable {
                onTouchMessage.invoke(msg)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        UserAvatar(
            name = getNameUserChat(msg?.user),
            urlAvatar = msg?.user?.avatar ?: "",
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.Top)
        )
        AuthorAndTextMessage(
            msg = msg, modifier = Modifier
                .weight(1f),
            onTouchMessage = onTouchMessage
        )

    }
}

@Composable
fun AuthorAndTextMessage(
    msg: ChatMessage?,
    modifier: Modifier = Modifier,
    onTouchMessage: (ChatMessage?) -> Unit,
) {
    Box(modifier = modifier.padding(start = 10.dp)) {
        ChatItemBubble(message = msg, onTouchMessage = onTouchMessage)
    }
}


@Composable
fun ChatItemBubble(message: ChatMessage?, onTouchMessage: (ChatMessage?) -> Unit) {

    Row(modifier = Modifier) {
        Column(modifier = Modifier) {
            Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = getNameUserChat(message?.user),
                    style = MaterialTheme.typography.h4.copy(
                        color = White,
                        fontSize = 14.sp,
                    ),
                    modifier = Modifier.weight(1f), maxLines = 1,
                )
                Row(modifier = Modifier.padding(start = 5.dp)) {

                    if (message?.isHost == true) {
                        Text(
                            text = stringResource(R.string.lb_host),
                            style = MaterialTheme.typography.subtitle1.copy(
                                color = White, fontSize = 10.sp
                            ),
                            modifier = Modifier
                                .background(
                                    color = Color_BLUE_7F4,
                                    shape = RoundedCornerShape(2.dp)
                                )
                                .padding(horizontal = 2.dp)
                        )
                    }

                    if (message?.objectData?.id != null && message.objectData?.id!! > 0) {
                        Image(painter = painterResource(id = getTypeDonation(message.objectData).resource),
                            contentDescription = "icon",
                            modifier = Modifier.size(30.dp))
                    }

                    if (message?.reactions != null && message.reactions?.isNotEmpty() == true) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_like_comment),
                            contentDescription = null, tint = Color.Unspecified,
                            modifier = Modifier
                        )
                    }
                }


            }
            ClickableMessage(message = message, onTouchMessage = onTouchMessage)
        }

        if (message?.type == MessageType.TIP_PACKAGE.value) {
            Spacer(modifier = Modifier.width(12.dp))
            AsyncImage(
                model = message.objectData?.image,
                contentDescription = "coin",
                modifier = Modifier.size(32.dp)
            )
        }
    }

}

@Composable
fun ClickableMessage(message: ChatMessage?, onTouchMessage: (ChatMessage?) -> Unit) {
    val styleMessage =
        if (message?.type == MessageType.TEXT.value) {
            messageFormatter(text = message.content + "", primary = false)
        } else messageFormatter(text = message?.objectData?.name ?: "", primary = false)

    ClickableText(
        text = styleMessage, onClick = {
            onTouchMessage.invoke(message)
        }, style = MaterialTheme.typography.subtitle1.copy(
            color = White,
            fontSize = 14.sp
        ), modifier = Modifier.padding()
    )
}

private val JumpToBottomThreshold = 56.dp

@Composable
fun ViewInputChat(modifier: Modifier, backgroundColor: Color, onComment: () -> Unit) {
    Row(
        modifier = modifier
            .height(40.dp)
            .background(shape = RoundedCornerShape(50.dp), color = backgroundColor)
            .clip(shape = RoundedCornerShape(50.dp))
            .clickable {
                onComment.invoke()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.lb_add_comment),
            style = MaterialTheme.typography.subtitle1.copy(
                color = White, fontSize = 12.sp
            ),
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}
