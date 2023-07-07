package olmo.wellness.android.domain.use_case.socket.private_chat

import arrow.core.Either
import olmo.wellness.android.core.BaseUseCase
import olmo.wellness.android.data.model.chat.request.EventRtc
import olmo.wellness.android.domain.repository.RTCPrivateChatRepository
import javax.inject.Inject

class SendRoomInfoPCUseCase  @Inject constructor(private val repository: RTCPrivateChatRepository)  : BaseUseCase<EventRtc,Unit>() {
    override fun execute(arg: EventRtc): Either<Exception, Unit> = repository.sendJoinRoom(arg)
}