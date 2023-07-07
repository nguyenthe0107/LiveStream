package olmo.wellness.android.domain.repository

import arrow.core.Either
import kotlinx.coroutines.flow.StateFlow
import olmo.wellness.android.data.model.chat.ConnectionState

interface RTCConnectionRepository {
    val connectionState: Either<Exception, StateFlow<ConnectionState?>>
    fun requestSession(token : String?): Either<Exception, Unit>
    fun disconnectSession(): Either<Exception, Unit>
    fun sendPingPong(): Either<Exception,Unit>
    fun getPingPong(): Either<Exception,StateFlow<Long?>>
}