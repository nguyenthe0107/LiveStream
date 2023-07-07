package olmo.wellness.android.data.repository

import arrow.core.Either
import kotlinx.coroutines.flow.StateFlow
import olmo.wellness.android.data.model.chat.ChatMessage
import olmo.wellness.android.data.model.chat.ReactionCount
import olmo.wellness.android.data.model.chat.request.EventRtc
import olmo.wellness.android.data.model.chat.room.DetailRoom
import olmo.wellness.android.data.model.chat.room.User
import olmo.wellness.android.domain.repository.RTCLiveStreamRepository
import olmo.wellness.android.extension.withAvailableNetwork
import olmo.wellness.android.webrtc.network.NetworkHandler
import olmo.wellness.android.webrtc.rtc.RtcServiceApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RTCLiveStreamRepositoryImpl @Inject constructor(
    private val networkHandler: NetworkHandler,
    private val service: RtcServiceApi
) : RTCLiveStreamRepository {
    override fun messages(): Either<Exception, StateFlow<List<ChatMessage>>> = networkHandler.withAvailableNetwork(service.messagesLiveStream())
    override fun chatMessage(): Either<Exception, StateFlow<ChatMessage?>> = networkHandler.withAvailableNetwork(service.chatMessageLiveStream())
    override fun reactionRoom(): Either<Exception, StateFlow<ReactionCount?>> = networkHandler.withAvailableNetwork(service.reactionRoomLiveStream())
    override fun reactionMessageRoom(): Either<Exception, StateFlow<ChatMessage?>> =networkHandler.withAvailableNetwork(service.reactionMessageRoomLiveStream())
    override fun sendJoinRoom(event: EventRtc): Either<Exception, Unit> = networkHandler.withAvailableNetwork(service.sendJoinRoomLiveStream(event))
    override fun sendReaction(event: EventRtc): Either<Exception, Unit>  = networkHandler.withAvailableNetwork(service.sendReactionLiveStream(event))
    override fun sendReactionMessageRoom(event: EventRtc): Either<Exception, Unit> = networkHandler.withAvailableNetwork(service.sendReactionMessageRoomLiveStream(event))
    override fun roomDetail(): Either<Exception, StateFlow<DetailRoom?>>  = networkHandler.withAvailableNetwork(service.roomDetailLiveStream())
    override fun sendMessage(event : EventRtc) = networkHandler.withAvailableNetwork(service.sendMessageLiveStream(event))
    override fun sendLeaveRoom(event: EventRtc): Either<Exception, Unit>  = networkHandler.withAvailableNetwork(service.sendLeaveRoomLiveStream(event))
    override fun sendLoadMoreMessage(event: EventRtc): Either<Exception, Unit> = networkHandler.withAvailableNetwork(service.sendLoadMoreLiveStream(event))
    override fun sendTipPackage(event: EventRtc): Either<Exception, Unit> = networkHandler.withAvailableNetwork(service.sendTipPackage(event))
    override fun userJoinRoom(): Either<Exception, StateFlow<User?>> =  networkHandler.withAvailableNetwork(service.userJoinRoomLiveStream())
    override fun userLeaveRoom(): Either<Exception, StateFlow<User?>> = networkHandler.withAvailableNetwork(service.userLeaveRoomLiveStream())
    override fun tipPackage(): Either<Exception, StateFlow<ChatMessage?>> = networkHandler.withAvailableNetwork(service.tipPackage())

}