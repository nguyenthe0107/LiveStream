package olmo.wellness.android.ui.chat.conversation_detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.data.model.chat.ChatMessage
import olmo.wellness.android.data.model.chat.MessageType
import olmo.wellness.android.extension.OnBottomReached
import olmo.wellness.android.extension.getNameUserChat
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.common.*
import olmo.wellness.android.ui.common.avatar.UserAvatar
import olmo.wellness.android.ui.helpers.DateTimeHelper
import olmo.wellness.android.ui.livestream.chatlivestream.cell.JumpToBottom
import olmo.wellness.android.ui.livestream.chatlivestream.view.ConversationTestTag
import olmo.wellness.android.ui.theme.*

val IDMe = sharedPrefs.getUserInfoLocal().userId

@Composable
fun MessageItem(
    messages: List<ChatMessage?>?,
    modifier: Modifier = Modifier,
    onLoadMore: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()


    Box(modifier = modifier) {

        LazyColumn(
            reverseLayout = true,
            state = scrollState,
            // Add content padding so that the content can be scrolled (y-axis)
            // below the status bar + app bar
            // TODO: Get height from somewhere
            contentPadding = WindowInsets.statusBars.add(WindowInsets(top = 90.dp))
                .asPaddingValues(),
            modifier = Modifier
                .testTag(ConversationTestTag)
                .padding(horizontal = 12.dp)
//                .fillMaxSize()
        ) {
            messages?.indices?.let { range ->
                for (index in range) {
                    val prevAuthor = messages.getOrNull(index - 1)?.userId
                    val nextAuthor = messages.getOrNull(index + 1)?.userId
                    val message = messages[index]
                    val isFirstMessageByAuthor = prevAuthor != message?.userId
                    val isLastMessageByAuthor = nextAuthor != message?.userId

                    val preDay = messages.getOrNull(index + 1)?.createdAtTimestamp
                    val messageDay = message?.createdAtTimestamp
                    item {

                        MessageDetail(
                            onAuthorClick = { name -> },
                            msg = message,
                            isUserMe = (message?.userId == IDMe),
                            isFirstMessageByAuthor = isFirstMessageByAuthor,
                            isLastMessageByAuthor = isLastMessageByAuthor || preDay == null || !DateTimeHelper.checkSameDate(
                                preDay,
                                messageDay))
                    }

                    if (preDay != null && !DateTimeHelper.checkSameDate(preDay, messageDay)) {
                        ItemDate(messageDay)
                    } else if (preDay == null) {
                        ItemDate(messageDay)
                    }
                }

            }
        }

        scrollState.OnBottomReached {
            onLoadMore.invoke()
        }
        // Jump to bottom button shows up when user scrolls past a threshold.
        // Convert to pixels:
        val jumpThreshold = with(LocalDensity.current) {
            JumpToBottomThreshold.toPx()
        }

        // Show the button if the first visible item is not the first one or if the offset is
        // greater than the threshold.
        val jumpToBottomButtonEnabled by remember {
            derivedStateOf {
                scrollState.firstVisibleItemIndex != 0 ||
                        scrollState.firstVisibleItemScrollOffset > jumpThreshold
            }
        }
        JumpToBottom(
            // Only show if the scroller is not at the bottom
            enabled = jumpToBottomButtonEnabled,
            onClicked = {
                scope.launch {
                    scrollState.animateScrollToItem(0)
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

private fun LazyListScope.ItemDate(messageDay: Long?) {
    if (DateTimeHelper.checkToday(messageDay)) {
        item {
            DayHeader("TODAY")
        }
    } else {
        item {
            DayHeader(DateTimeHelper.convertToStringDate(messageDay))
        }
    }
}


@Composable
fun MessageDetail(
    onAuthorClick: (String) -> Unit,
    msg: ChatMessage?,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
) {
    val borderColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.tertiary
    }

//    val spaceBetweenAuthors = if (isLastMessageByAuthor) Modifier.padding(top = 8.dp) else Modifier
    val spaceBetweenAuthors = Modifier
//    var direction = LocalLayoutDirection provides  LayoutDirection.Rtl
//    if (!isUserMe) direction= LocalLayoutDirection provides LayoutDirection.Ltr
    var direction = (if (isUserMe) {
        LocalLayoutDirection provides LayoutDirection.Rtl
    } else {
        LocalLayoutDirection provides LayoutDirection.Ltr
    })
    CompositionLocalProvider(direction) {
        Row(modifier = spaceBetweenAuthors) {
            if (isLastMessageByAuthor) {
                CompositionLocalProvider(
                    LocalLayoutDirection provides LayoutDirection.Ltr
                ) {
                    UserAvatar(
                        name = getNameUserChat(msg?.user),
                        urlAvatar = msg?.user?.avatar ?: "",
                        isOnline = true,
                        modifier = Modifier
                            .size(26.dp)
                            .align(Alignment.Top)
                    )
                }
            } else {
                // Space under avatar
                Spacer(modifier = Modifier.width(26.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                when (msg?.type) {
                    MessageType.TEXT.value -> {
                        AuthorAndTextMessage(
                            msg = msg,
                            isUserMe = isUserMe,
                            isFirstMessageByAuthor = isFirstMessageByAuthor,
                            isLastMessageByAuthor = isLastMessageByAuthor,
                            authorClicked = onAuthorClick,
                            modifier = Modifier
                                .padding(end = 64.dp)

                        )
                    }
                    MessageType.FILE.value -> {
                        ImagesMessage(
                            msg = msg,
                            isUserMe = isUserMe,
                            isFirstMessageByAuthor = isFirstMessageByAuthor,
                            isLastMessageByAuthor = isLastMessageByAuthor,
                            authorClicked = onAuthorClick,
                            modifier = Modifier
                                .padding(end = 64.dp)
//                                .weight(1f)
                        )
                    }
                }

                Box(modifier = Modifier) {
//                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Text(
                        text = DateTimeHelper.convertToStringHour(msg?.createdAtTimestamp),
                        style =
                        androidx.compose.material.MaterialTheme.typography.subtitle1.copy(
                            fontSize = 12.sp, color = Neutral_Gray_6
                        ),
                        modifier = Modifier
                            .padding(start = 10.dp),
                    )
//                    }
                }
                if (isFirstMessageByAuthor) {
                    // Last bubble before next author
                    Spacer(modifier = Modifier.height(35.dp))
                } else {
                    // Between bubbles
                    Spacer(modifier = Modifier.height(15.dp))
                }


            }

        }
    }
}


@Composable
fun ImagesMessage(
    msg: ChatMessage?,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
    authorClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    msg?.objectData?.files?.let { images ->
        Column(modifier = modifier) {
            for (i in images.indices) {
                if (i % 2 == 0) {
                    Row(modifier = Modifier) {
                        Box(modifier = Modifier.padding(start = 10.dp)) {
                            ItemImage(images[i]?.url)
                        }
                        if (i + 1 < images.size) {
                            Box(modifier = Modifier.padding(start = 10.dp)) {
                                ItemImage(images[i + 1]?.url)
                            }
                        }
                    }
                    if (i!= images.size-1){
                     Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun AuthorAndTextMessage(
    msg: ChatMessage?,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
    authorClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
//        if (isLastMessageByAuthor) {
//            AuthorNameTimestamp(msg)
//        }
        ChatItemBubble(msg, isUserMe, authorClicked = authorClicked)

//        if (isFirstMessageByAuthor) {
//            // Last bubble before next author
//            Spacer(modifier = Modifier.height(20.dp))
//        } else {
//            // Between bubbles
//            Spacer(modifier = Modifier.height(5.dp))
//        }
    }
}

@Composable
fun ItemImage(
    url: String?,
) {
    Column() {
        RoundedAsyncImage(
            imageUrl = url ?: "", cornerRadius = 10.dp,
            modifier = Modifier
                .size(100.dp), contentScale = ContentScale.FillWidth,
            resDefault = R.drawable.olmo_img_onboard_right,
            size = 100.dp
        )
    }
}

@Composable
fun ChatItemBubble(
    message: ChatMessage?,
    isUserMe: Boolean,
    authorClicked: (String) -> Unit,
) {
    val backgroundBubbleColor = if (isUserMe) {
        Color_4F4
    } else {
        Color_blue_Blur.copy(
            alpha = 0.12f
        )
    }

    Column {
        Surface(
            color = backgroundBubbleColor,
            shape = ChatBubbleShape,
            shadowElevation = 0.dp,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Column() {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    ClickableMessage(
                        message = message,
                        isUserMe = isUserMe,
                        authorClicked = authorClicked
                    )
                }

                //time -> disable
//                Box(modifier = Modifier) {
//                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
//                        Text(
//                            text = DateTimeHelper.convertToStringHour(message?.createdAtTimestamp),
//                            style =
//                            androidx.compose.material.MaterialTheme.typography.subtitle1.copy(
//                                fontSize = 12.sp, color = Neutral_Gray_6
//                            ),
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(10.dp),
//                        )
//                    }
//                }

            }
        }

// Image
//        message.image?.let {
//            Spacer(modifier = Modifier.height(4.dp))
//            Surface(
//                color = backgroundBubbleColor,
//                shape = ChatBubbleShape
//            ) {
//                Image(
//                    painter = painterResource(it),
//                    contentScale = ContentScale.Fit,
//                    modifier = Modifier.size(160.dp),
//                    contentDescription = null
//                )
//            }
//        }
    }
}

@Composable
fun ClickableMessage(
    message: ChatMessage?,
    isUserMe: Boolean,
    authorClicked: (String) -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    val styledMessage = message?.content?.let {
        messageFormatter(
            text = it,
            primary = isUserMe
        )
    }

    styledMessage?.let {
        ClickableText(
            text = it,
            style = androidx.compose.material.MaterialTheme.typography.subtitle1.copy(
                fontSize = 12.sp, color = Black_037, lineHeight = 14.sp
            ),
            modifier = Modifier.padding(8.dp),
            onClick = {
                styledMessage
                    .getStringAnnotations(start = it, end = it)
                    .firstOrNull()
                    ?.let { annotation ->
                        when (annotation.tag) {
                            SymbolAnnotationType.LINK.name -> uriHandler.openUri(annotation.item)
                            SymbolAnnotationType.PERSON.name -> authorClicked(annotation.item)
                            else -> Unit
                        }
                    }
            }
        )
    }
}

@Composable
private fun AuthorNameTimestamp(msg: ChatMessage) {
    // Combine author and timestamp for a11y.
    Row(modifier = Modifier.semantics(mergeDescendants = true) {}) {
        Text(
            text = getNameUserChat(msg.user),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .alignBy(LastBaseline)
                .paddingFrom(LastBaseline, after = 8.dp) // Space to 1st bubble
        )
        Spacer(modifier = Modifier.width(8.dp))
//        Text(
//            text = msg.timestamp,
//            style = MaterialTheme.typography.bodySmall,
//            modifier = Modifier.alignBy(LastBaseline),
//            color = MaterialTheme.colorScheme.onSurfaceVariant
//        )
    }
}

//private val ChatBubbleShape = RoundedCornerShape(0.dp, 16.dp, 16.dp, 16.dp)
private val ChatBubbleShape = RoundedCornerShape(8.dp, 8.dp, 8.dp, 8.dp)

@Composable
fun DayHeader(dayString: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 15.dp, horizontal = 16.dp)
            .height(16.dp)
    ) {
        DayHeaderLine()
        Text(
            text = dayString,
            modifier = Modifier.padding(horizontal = 10.dp),
            style = androidx.compose.material.MaterialTheme.typography.subtitle2.copy(
                fontSize = 14.sp, color = Neutral_Gray_6
            )
        )
        DayHeaderLine()
    }
}

@Composable
private fun RowScope.DayHeaderLine() {
    Divider(
        modifier = Modifier
            .weight(1f)
            .align(Alignment.CenterVertically),
        color = Gray_EF3
    )
}

private val JumpToBottomThreshold = 56.dp
