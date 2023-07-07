package olmo.wellness.android.domain.repository

import arrow.core.Either
import kotlinx.coroutines.flow.StateFlow
import olmo.wellness.android.data.model.chat.ChatMessage
import olmo.wellness.android.data.model.chat.ReactionCount
import olmo.wellness.android.data.model.chat.request.EventRtc
import olmo.wellness.android.data.model.chat.room.DetailRoom
import olmo.wellness.android.data.model.chat.room.User

interface RTCPrivateChatRepository {
    fun messages(): Either<Exception, StateFlow<List<ChatMessage>>>
    fun chatMessage() : Either<Exception, StateFlow<ChatMessage?>>
    fun reactionMessageRoom() : Either<Exception, StateFlow<ChatMessage?>>
    fun sendJoinRoom(event : EventRtc) : Either<Exception, Unit>
//    fun sendReaction(event: EventRtc) : Either<Exception, Unit>
    fun sendReactionMessageRoom(event: EventRtc) : Either<Exception, Unit>
    fun getRoomDetail() : Either<Exception, StateFlow<DetailRoom?>>
    fun sendMessage(event : EventRtc): Either<Exception, Unit>
    fun sendLeaveRoom(event: EventRtc) : Either<Exception, Unit>
    fun sendLoadMoreMessage(event: EventRtc) : Either<Exception, Unit>
    fun userJoinRoom() : Either<Exception, StateFlow<User?>>
    fun userLeaveRoom() : Either<Exception, StateFlow<User?>>
}