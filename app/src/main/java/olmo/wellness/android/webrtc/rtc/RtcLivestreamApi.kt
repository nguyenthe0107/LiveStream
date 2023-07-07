package olmo.wellness.android.webrtc.rtc

import kotlinx.coroutines.flow.StateFlow
import olmo.wellness.android.data.model.chat.ChatMessage
import olmo.wellness.android.data.model.chat.ReactionCount
import olmo.wellness.android.data.model.chat.request.EventRtc
import olmo.wellness.android.data.model.chat.room.DetailRoom
import olmo.wellness.android.data.model.chat.room.User

interface RtcLivestreamApi {
    fun messagesLiveStream(): StateFlow<List<ChatMessage>>
    fun chatMessageLiveStream(): StateFlow<ChatMessage?>
    fun reactionRoomLiveStream() : StateFlow<ReactionCount?>
    fun reactionMessageRoomLiveStream(): StateFlow<ChatMessage?>
    fun sendJoinRoomLiveStream(event : EventRtc)
    fun sendReactionLiveStream(event: EventRtc)
    fun sendReactionMessageRoomLiveStream(event: EventRtc)
    fun sendTipPackage(event: EventRtc)
    fun roomDetailLiveStream() :  StateFlow<DetailRoom?>
    fun sendMessageLiveStream(event : EventRtc)
    fun sendLeaveRoomLiveStream(event : EventRtc)
    fun sendLoadMoreLiveStream(event: EventRtc)
    fun userJoinRoomLiveStream() : StateFlow<User?>
    fun userLeaveRoomLiveStream() : StateFlow<User?>
    fun tipPackage() : StateFlow<ChatMessage?>
}