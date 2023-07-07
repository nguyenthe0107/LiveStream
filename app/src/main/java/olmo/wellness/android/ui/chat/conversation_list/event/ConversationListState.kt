package olmo.wellness.android.ui.chat.conversation_list.event

import olmo.wellness.android.data.model.chat.room.DetailRoom
import olmo.wellness.android.ui.chat.private_chat.event.PrivateChatEvent

data class ConversationListState(
    val showLoading: Boolean = false,
    val listRoomConversation: List<DetailRoom>?=null
)

sealed class ConversationListEvent {
    data class ShowLoading(val isLoading: Boolean = false) : ConversationListEvent()

    data class UpdateListRoomConversation(val listRoom: List<DetailRoom>?) : ConversationListEvent()
}