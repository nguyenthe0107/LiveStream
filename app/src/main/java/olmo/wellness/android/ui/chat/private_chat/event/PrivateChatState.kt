package olmo.wellness.android.ui.chat.private_chat.event

import olmo.wellness.android.data.model.chat.ChatMessage
import olmo.wellness.android.ui.livestream.chatlivestream.state.ChatLivestreamUiState

data class PrivateChatState (
    val showLoading: Boolean = false,
    val roomStream: ChatLivestreamUiState? = null,
)

sealed class PrivateChatEvent{
    data class ShowLoading(val isLoading: Boolean = false) : PrivateChatEvent()

    data class UpdateListChat(val listChat: List<ChatMessage>?) : PrivateChatEvent()

    data class UpdateRoomDetail(val chatLivestreamUiState: ChatLivestreamUiState?) : PrivateChatEvent()
}