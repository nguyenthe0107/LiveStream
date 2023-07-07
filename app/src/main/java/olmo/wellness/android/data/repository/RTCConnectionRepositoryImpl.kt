package olmo.wellness.android.data.repository

import arrow.core.Either
import kotlinx.coroutines.flow.StateFlow
import olmo.wellness.android.data.model.chat.ConnectionState
import olmo.wellness.android.domain.repository.RTCConnectionRepository
import olmo.wellness.android.extension.withAvailableNetwork
import olmo.wellness.android.webrtc.network.NetworkHandler
import olmo.wellness.android.webrtc.rtc.RtcServiceApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RTCConnectionRepositoryImpl @Inject constructor(
    private val networkHandler: NetworkHandler,
    private val service: RtcServiceApi
) : RTCConnectionRepository {
    override val connectionState: Either<Exception, StateFlow<ConnectionState?>> =
        networkHandler.withAvailableNetwork(service.connectionState)

    override fun requestSession(token: String?): Either<Exception, Unit> =
        networkHandler.withAvailableNetwork(service.requestSession(token))

    override fun disconnectSession(): Either<Exception, Unit> =
        networkHandler.withAvailableNetwork(service.disconnectSession())

    override fun sendPingPong(): Either<Exception, Unit>  = networkHandler.withAvailableNetwork(service.sendPingPong())

    override fun getPingPong(): Either<Exception, StateFlow<Long?>> = networkHandler.withAvailableNetwork(service.pingPong())

}