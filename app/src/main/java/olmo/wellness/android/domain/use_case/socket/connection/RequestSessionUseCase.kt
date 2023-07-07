package olmo.wellness.android.domain.use_case.socket.connection

import arrow.core.Either
import olmo.wellness.android.core.BaseUseCase
import olmo.wellness.android.domain.repository.RTCConnectionRepository
import javax.inject.Inject

class RequestSessionUseCase @Inject constructor(val repository: RTCConnectionRepository) : BaseUseCase<String?,Unit>() {
    override fun execute(arg: String?): Either<Exception, Unit> = repository.requestSession(arg)
}