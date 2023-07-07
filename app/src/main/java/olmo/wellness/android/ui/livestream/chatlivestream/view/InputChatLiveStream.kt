package olmo.wellness.android.ui.livestream.chatlivestream.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import olmo.wellness.android.R
import olmo.wellness.android.data.model.chat.ChatMessage
import olmo.wellness.android.data.model.chat.room.User
import olmo.wellness.android.extension.clearFocusOnKeyboardDismiss
import olmo.wellness.android.extension.getNameUserChat
import olmo.wellness.android.ui.livestream.chatlivestream.cell.UserInput
import olmo.wellness.android.ui.livestream.view.streamer.LiveStreamerViewModel
import olmo.wellness.android.ui.theme.Neutral_Gray_5
import olmo.wellness.android.ui.theme.White


@SuppressLint("CoroutineCreationDuringComposition", "SuspiciousIndentation")
@Composable
fun InputChatLiveStream(
    isShowAddComment: MutableState<Boolean>?,
    focusRequester: FocusRequester,
    focusManager: FocusManager,
    isInputChat: MutableState<Boolean>?,
    scope: CoroutineScope,
    textMessageValueState: MutableState<TextFieldValue>,
    replyID: String?,
    sendMessage: ((String, String?) -> Unit)?,  //text, replyID
) {
    isShowAddComment?.value = false


    UserInput(onMessageSent = {
        if (it.isNotBlank()) {
            sendMessage?.invoke(it, replyID)
        }
        clearInputChat(focusManager, isInputChat, isShowAddComment, textMessageValueState)
    }, isAutoTextFocus = true,
        textMessageValueState = textMessageValueState,
        onClearFocusOnKeyboardDismiss = {
            clearInputChat(focusManager, isInputChat, isShowAddComment, textMessageValueState)
        })

}


fun clearInputChat(
    focusManager: FocusManager,
    isInputChat: MutableState<Boolean>?,
    isShowAddComment: MutableState<Boolean>?,
    textMessageValueState: MutableState<TextFieldValue>?,
) {
    focusManager.clearFocus()
    isInputChat?.value = false
    isShowAddComment?.value = true
    if (textMessageValueState?.value != null) {
        textMessageValueState.value = textMessageValueState.value.copy(
            text = "",
            selection = TextRange(0)
        )
    }

}

fun dismissActionMessage(
    msgSelect: MutableState<ChatMessage?>,
    openDialogActionMessage: MutableState<Boolean>,
    isClearMsg: Boolean = true,
) {
    if (isClearMsg) {
        msgSelect.value = null
    }
    openDialogActionMessage.value = false
}


fun convertTextReply(user: User?): TextFieldValue {
    val newText = "@" + getNameUserChat(user) + " "
    return TextFieldValue(
        text = newText,
        selection = TextRange(newText.length)
    )

}