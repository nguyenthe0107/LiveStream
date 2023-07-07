package olmo.wellness.android.webrtc.socket

import android.text.TextUtils
import android.util.Log
import kotlinx.coroutines.channels.Channel
import okhttp3.*
import okio.ByteString
import olmo.wellness.android.BuildConfig
import olmo.wellness.android.core.Constants
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.webrtc.rtc.RtcConstant
import olmo.wellness.android.webrtc.rtc.RtcService
import org.json.JSONObject
import javax.inject.Inject

class SocketService @Inject constructor() : SocketApi {

    companion object {
        const val DEFAULT_CODE = 1000
    }

    private var DEFAULT_URL: String?=null

    private var client = OkHttpClient.Builder().retryOnConnectionFailure(true).build()
    private var request : Request?=null


    private var socket: WebSocket? = null
    override val messages = Channel<String>(Channel.CONFLATED)

    private val listener = object : DefaultSocketListener() {

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            if (text.isNotBlank()){
                messages.trySend(text)
            }
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            super.onMessage(webSocket, bytes)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            Log.d(RtcService.TAG , "Error - ${response?.toString()}")
        }
    }

    private fun createSocket(request: Request, listener: WebSocketListener) =
        client.newWebSocket(request, listener)

    override fun sendMessage(type: String, body: JSONObject) {
        val send= JSONObject().apply {
            put("type", type)
            put("data", body)
        }.toString()
        Log.d(RtcService.TAG , "Send - $send")
        socket?.send(send)

    }

    override fun sendMessage(type: String) {
        val send =JSONObject().apply {
            put("type",type)
        }.toString()
        socket?.send(send)
        Log.d(RtcService.TAG , "Send - $send")
    }

    override fun sendMessage(type: String, body: Int) {
        val send= JSONObject().apply {
            put("type", type)
            put("data", body)
        }.toString()
        Log.d(RtcService.TAG , "Send - $send")
        socket?.send(send)
    }


    override fun connect(token: String?) {
        if (!TextUtils.isEmpty(token)){
            DEFAULT_URL=provideUrl()
            DEFAULT_URL+="?token=$token"
            Log.d(RtcService.TAG, DEFAULT_URL!!)
            DEFAULT_URL?.let {
                request = Request.Builder().apply { url(it) }
                    .build()
                socket = createSocket(request!!, listener)
                sendMessage(RtcConstant.READY)
            }
        }
    }

    override fun disconnect() {
        socket?.close(DEFAULT_CODE, DEFAULT_CODE.toString())
        socket = null
    }

    private fun provideUrl() : String{
        val configAppModel = sharedPrefs.getConfigApp()
        var apiUrl = Constants.SERVER_URL_DEV
        val appNumber = BuildConfig.VERSION_CODE
        apiUrl = when {
            appNumber >= configAppModel.android?.dev ?: 0 -> {
                Constants.SOCKET_URL_DEV
            }
            appNumber >= configAppModel.android?.staging ?: 0 && appNumber < configAppModel.android?.dev ?: 0  -> {
                Constants.SOCKET_URL_STAG
            }
            else -> {
                Constants.SOCKET_URL_PRO
            }
        }
        return apiUrl
    }
}