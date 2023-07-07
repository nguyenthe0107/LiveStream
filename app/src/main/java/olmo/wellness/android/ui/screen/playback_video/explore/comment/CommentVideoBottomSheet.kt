package olmo.wellness.android.ui.screen.playback_video.explore.comment

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.dp
import olmo.wellness.android.data.model.chat.ChatMessage
import olmo.wellness.android.ui.common.empty.EmptyBottomSheet
import olmo.wellness.android.ui.livestream.chatlivestream.state.ChatLivestreamUiState
import olmo.wellness.android.ui.livestream.comment.view.CommentContent
import olmo.wellness.android.ui.theme.White

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CommentVideoBottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    messageReply: MutableState<ChatMessage?>? = null,
    isInputChat: MutableState<Boolean>? = null,
    isShowAddComment: MutableState<Boolean>? = null,
    roomChatState: ChatLivestreamUiState?,
    onReaction : (ChatMessage?)->Unit,
    ) {
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = White,
        sheetContent = {
            if (modalBottomSheetState.isVisible) {
                CommentContent(
                    roomChatState = roomChatState,
                    isInputChat = isInputChat,
                    isShowAddComment = isShowAddComment,
                    messageReply = messageReply,
                    onReaction = onReaction
                )
            }else{
                EmptyBottomSheet()
            }
        }) {
    }
}