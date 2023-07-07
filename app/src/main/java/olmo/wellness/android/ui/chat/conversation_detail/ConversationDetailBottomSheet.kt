package olmo.wellness.android.ui.chat.conversation_detail

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ExperimentalAnimatedInsets
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import olmo.wellness.android.R
import olmo.wellness.android.data.model.chat.room.User
import olmo.wellness.android.extension.getNameUserChat
import olmo.wellness.android.ui.common.avatar.UserAvatar
import olmo.wellness.android.ui.common.empty.EmptyBottomSheet
import olmo.wellness.android.ui.livestream.chatlivestream.cell.UserInput
import olmo.wellness.android.ui.livestream.chatlivestream.state.ChatLivestreamUiState
import olmo.wellness.android.ui.screen.capture_screen.CaptureScreen
import olmo.wellness.android.ui.theme.*


@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimatedInsets::class,
    ExperimentalPermissionsApi::class)
@Composable
fun ConversationDetailBottomSheet(
    roomChatState: ChatLivestreamUiState?,
    user: User? = null,
    modalBottomSheetState: ModalBottomSheetState,
    onSendMessage: (String) -> Unit,
    onImageSent: (List<String>) -> Unit,
    onLoadMore: () -> Unit
) {

    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()


    var isCaptureDisplay by remember { mutableStateOf(false) }

    val imgTakePhoto = remember {
        mutableStateOf<Uri?>(null)
    }


    val user = roomChatState?.getUserChatPrivate()?.collectAsState()

    val textMessage = remember {
        mutableStateOf("")
    }

    val focusManager = LocalFocusManager.current

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = White,
        sheetContentColor = Transparent,
        sheetContent = {
            if (modalBottomSheetState.isVisible) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(0.8f),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .background(
                                color = White,
                                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                            )
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        UserAvatar(
                            isOnline = user?.value?.isOnline ?: false,
                            name = getNameUserChat(user?.value),
                            urlAvatar = user?.value?.avatar ?: "",
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .size(43.dp)
                        )

                        Column(modifier = Modifier) {
                            Text(
                                text = getNameUserChat(user?.value),
                                style = MaterialTheme.typography.subtitle2
                            )
                            if (user?.value?.isOnline == true) {
                                Text(
                                    text = stringResource(R.string.lb_active_now),
                                    style = MaterialTheme.typography.subtitle2.copy(
                                        color = Success_500
                                    )
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(color = Neutral_Gray)
                    ) {
                        ConversationContent(roomChatState?.messages, onLoadMore = onLoadMore)
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
//                            .height(90.dp)
                            .background(color = White.copy(alpha = 0.95f))
                            .shadow(elevation = 20.dp)
                    ) {
                        Box(
//                            modifier = Modifier.padding(top = 18.dp, end = 20.dp, start = 20.dp)
                        ) {
                            UserInput(onMessageSent = {
                                textMessage.value=it
                                if (textMessage.value.isNotBlank()) {
                                    onSendMessage.invoke(textMessage.value)
                                }
                                textMessage.value = ""
                            }, roomId = roomChatState?.getRoomId(), onImageSent = {
                                onImageSent.invoke(it)
                            }, isPrivateChat = true, isOpenCaptureImage = {
                                isCaptureDisplay = true
                            }, imgTakePhoto = imgTakePhoto)
//                            TextField(
//                                value = textMessage.value,
//                                onValueChange = {
//                                    textMessage.value = it
//                                },
//                                placeholder = {
//                                    Text(
//                                        text = stringResource(R.string.lb_add_comment),
//                                        style = MaterialTheme.typography.caption.copy(
//                                            color = Neutral_Gray_5, fontSize = 14.sp
//                                        ),
//                                    )
//                                },
//                                colors = TextFieldDefaults.textFieldColors(
//                                    backgroundColor = White,
//                                    textColor = Color_Green_Main,
//                                    unfocusedIndicatorColor = Color.Transparent,
//                                    focusedIndicatorColor = Color.Transparent,
//                                    cursorColor = Color_Green_Main
//                                ),
//                                textStyle = MaterialTheme.typography.subtitle1,
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .height(50.dp)
//                                    .background(
//                                        color = White,
//                                        shape = RoundedCornerShape(50.dp)
//                                    )
//                                    .border(
//                                        BorderStroke(width = 1.dp, color = Color_Green_Main),
//                                        shape = RoundedCornerShape(50)
//                                    )
//                                    .clip(RoundedCornerShape(50.dp))
//                                    .clearFocusOnKeyboardDismiss(onKeyboardDismiss = {
//                                        focusManager.clearFocus()
//                                    }),
//                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
//                                keyboardActions = KeyboardActions(
//                                    onSend = {
//                                        if (textMessage.value.isNotBlank()) {
//                                            onSendMessage.invoke(textMessage.value)
//                                        }
//                                        textMessage.value = ""
//                                        focusManager.clearFocus()
//                                    })
//
//                            )
                        }
                    }
                }
            } else {
                EmptyBottomSheet()
            }
        }
    ) {

    }

    CaptureScreen(isDisplay = isCaptureDisplay, callbackUri = { uriSelected ->
        uriSelected?.let {
            imgTakePhoto.value = it
        }
        isCaptureDisplay = false
    })
}