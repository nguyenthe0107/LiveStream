package olmo.wellness.android.domain.use_case.socket.connection

import arrow.core.Either
import kotlinx.coroutines.flow.StateFlow
import olmo.wellness.android.core.BaseUseCase
import olmo.wellness.android.data.model.chat.ConnectionState
import olmo.wellness.android.domain.repository.RTCConnectionRepository
import olmo.wellness.android.domain.repository.RTCLiveStreamRepository
import javax.inject.Inject

class GetConnectionStateUseCase @Inject constructor(private val repository: RTCConnectionRepository) : BaseUseCase<Unit, StateFlow<ConnectionState?>>() {
    override fun execute(arg: Unit): Either<Exception, StateFlow<ConnectionState?>> = repository.connectionState
}