package olmo.wellness.android.domain.use_case.socket.connection

import arrow.core.Either
import olmo.wellness.android.core.BaseUseCase
import olmo.wellness.android.domain.repository.RTCConnectionRepository
import olmo.wellness.android.domain.repository.RTCLiveStreamRepository
import javax.inject.Inject

class DisconnectSessionUseCase @Inject constructor(private val repository: RTCConnectionRepository) : BaseUseCase<Unit,Unit>() {
    override fun execute(arg: Unit): Either<Exception, Unit> = repository.disconnectSession()
}