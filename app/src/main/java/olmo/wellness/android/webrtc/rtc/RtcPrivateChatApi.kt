package olmo.wellness.android.webrtc.rtc

import kotlinx.coroutines.flow.StateFlow
import olmo.wellness.android.data.model.chat.ChatMessage
import olmo.wellness.android.data.model.chat.ReactionCount
import olmo.wellness.android.data.model.chat.request.EventRtc
import olmo.wellness.android.data.model.chat.room.DetailRoom
import olmo.wellness.android.data.model.chat.room.User

interface RtcPrivateChatApi {
    fun messagesPrivateChat(): StateFlow<List<ChatMessage>>
    fun chatMessagePrivateChat(): StateFlow<ChatMessage?>
//    fun reactionRoomPrivateChat() : StateFlow<ReactionCount?>
    fun reactionMessageRoomPrivateChat(): StateFlow<ChatMessage?>
    fun sendJoinRoomPrivateChat(event : EventRtc)
//    fun sendReactionLiveStream(event: EventRtc)
    fun sendReactionMessageRoomPrivateChat(event: EventRtc)
    fun getRoomDetailPrivateChat() : StateFlow<DetailRoom?>
    fun sendMessagePrivateChat(event : EventRtc)
    fun sendLeaveRoomPrivateChat(event : EventRtc)
    fun sendLoadMorePrivateChat(event: EventRtc)
    fun userJoinRoomPrivateChat() : StateFlow<User?>
    fun userLeaveRoomPrivateChat() : StateFlow<User?>
}
