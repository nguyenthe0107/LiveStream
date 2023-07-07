package olmo.wellness.android.domain.use_case.socket.livestream

import arrow.core.Either
import olmo.wellness.android.core.BaseUseCase
import olmo.wellness.android.data.model.chat.request.EventRtc
import olmo.wellness.android.domain.repository.RTCLiveStreamRepository
import javax.inject.Inject

class SendLeaveRoomLSUseCase @Inject constructor(private val repository: RTCLiveStreamRepository) : BaseUseCase<EventRtc,Unit>() {
    override fun execute(arg: EventRtc): Either<Exception, Unit> = repository.sendLeaveRoom(arg)
}