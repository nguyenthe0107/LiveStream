package olmo.wellness.android.data.repository

import arrow.core.Either
import kotlinx.coroutines.flow.StateFlow
import olmo.wellness.android.data.model.chat.ChatMessage
import olmo.wellness.android.data.model.chat.request.EventRtc
import olmo.wellness.android.data.model.chat.room.DetailRoom
import olmo.wellness.android.data.model.chat.room.User
import olmo.wellness.android.domain.repository.RTCPrivateChatRepository
import olmo.wellness.android.extension.withAvailableNetwork
import olmo.wellness.android.webrtc.network.NetworkHandler
import olmo.wellness.android.webrtc.rtc.RtcPrivateChatApi
import olmo.wellness.android.webrtc.rtc.RtcServiceApi
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RTCPrivateChatRepositoryImpl @Inject constructor(
    private val networkHandler: NetworkHandler,
    private val service: RtcServiceApi
) : RTCPrivateChatRepository {
    override fun messages(): Either<Exception, StateFlow<List<ChatMessage>>> = networkHandler.withAvailableNetwork(service.messagesPrivateChat())

    override fun chatMessage(): Either<Exception, StateFlow<ChatMessage?>> = networkHandler.withAvailableNetwork(service.chatMessagePrivateChat())
    override fun reactionMessageRoom(): Either<Exception, StateFlow<ChatMessage?>> =networkHandler.withAvailableNetwork(service.reactionMessageRoomPrivateChat())

    override fun sendJoinRoom(event: EventRtc): Either<Exception, Unit> = networkHandler.withAvailableNetwork(service.sendJoinRoomPrivateChat(event))

    override fun sendReactionMessageRoom(event: EventRtc): Either<Exception, Unit> = networkHandler.withAvailableNetwork(service.sendReactionMessageRoomPrivateChat(event))
    override fun getRoomDetail(): Either<Exception, StateFlow<DetailRoom?>> = networkHandler.withAvailableNetwork(service.getRoomDetailPrivateChat())

    override fun sendMessage(event: EventRtc): Either<Exception, Unit> = networkHandler.withAvailableNetwork(service.sendMessagePrivateChat(event))
    override fun sendLeaveRoom(event: EventRtc): Either<Exception, Unit> = networkHandler.withAvailableNetwork(service.sendLeaveRoomPrivateChat(event))

    override fun sendLoadMoreMessage(event: EventRtc): Either<Exception, Unit> = networkHandler.withAvailableNetwork(service.sendLoadMorePrivateChat(event))

    override fun userJoinRoom(): Either<Exception, StateFlow<User?>>  =  networkHandler.withAvailableNetwork(service.userJoinRoomPrivateChat())
    override fun userLeaveRoom(): Either<Exception, StateFlow<User?>> = networkHandler.withAvailableNetwork(service.userLeaveRoomPrivateChat())
}