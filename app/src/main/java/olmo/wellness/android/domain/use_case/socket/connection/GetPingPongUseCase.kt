package olmo.wellness.android.domain.use_case.socket.connection

import arrow.core.Either
import kotlinx.coroutines.flow.StateFlow
import olmo.wellness.android.core.BaseUseCase
import olmo.wellness.android.domain.repository.RTCConnectionRepository
import javax.inject.Inject

class GetPingPongUseCase @Inject constructor(val repository: RTCConnectionRepository) :
    BaseUseCase<Unit, StateFlow<Long?>>() {
    override fun execute(arg: Unit): Either<Exception, StateFlow<Long?>> = repository.getPingPong()
}