package olmo.wellness.android.ui.livestream.chatlivestream.event

import olmo.wellness.android.data.model.chat.ChatMessage
import olmo.wellness.android.data.model.chat.ReactionCount
import olmo.wellness.android.data.model.chat.room.User
import olmo.wellness.android.ui.livestream.chatlivestream.state.ChatLivestreamUiState

data class ChatLivestreamState(
    val showLoading: Boolean = false,
    val roomStream: ChatLivestreamUiState? = null,
    val isComment: Boolean = false,
    val userJoinRoom : User?=null
)

sealed class ChatLivestreamEvent {
    data class ShowLoading(val isLoading: Boolean = false) : ChatLivestreamEvent()

    data class UpdateListChat(val listChat: List<ChatMessage>?) : ChatLivestreamEvent()

    data class UpdateRoomDetail(val chatLivestreamUiState: ChatLivestreamUiState?) : ChatLivestreamEvent()

    data class UpdateHeatCount(val reactionCount: ReactionCount) : ChatLivestreamEvent()

    data class UpdateIsComment(val isComment: Boolean) : ChatLivestreamEvent()

    data class UpdateUserJoinRoom(val user : User) : ChatLivestreamEvent()

}