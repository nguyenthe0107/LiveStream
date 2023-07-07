package olmo.wellness.android.webrtc.socket

import kotlinx.coroutines.channels.Channel
import org.json.JSONObject

interface SocketApi {
    val messages: Channel<String>
    fun sendMessage(type: String, body: JSONObject = JSONObject())
    fun sendMessage(type: String)
    fun sendMessage(type: String,body: Int)
    fun connect(token: String?)
    fun disconnect()
}