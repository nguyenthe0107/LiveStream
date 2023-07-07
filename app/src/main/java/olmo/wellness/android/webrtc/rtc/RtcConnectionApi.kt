package olmo.wellness.android.webrtc.rtc

import kotlinx.coroutines.flow.StateFlow
import olmo.wellness.android.data.model.chat.ConnectionState

interface RtcConnectionApi {
    fun requestSession(token: String?)
    var connectionState : StateFlow<ConnectionState?>
    fun disconnectSession()
    fun pingPong() : StateFlow<Long?>
    fun sendPingPong()
}