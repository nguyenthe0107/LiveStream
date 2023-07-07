package olmo.wellness.android.domain.use_case.socket.booking

import arrow.core.Either
import olmo.wellness.android.core.BaseUseCase
import olmo.wellness.android.data.model.chat.request.EventRtc
import olmo.wellness.android.domain.repository.RTCBookingServiceRepository
import javax.inject.Inject

class SendAddServiceUseCase @Inject constructor(private val  repository: RTCBookingServiceRepository) : BaseUseCase<EventRtc,Unit>() {
    override fun execute(arg: EventRtc): Either<Exception, Unit>  = repository.sendAddService(arg)
}