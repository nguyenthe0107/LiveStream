package olmo.wellness.android.webrtc.rtc

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import olmo.wellness.android.data.model.chat.*
import olmo.wellness.android.data.model.chat.request.EventRtc
import olmo.wellness.android.data.model.chat.room.DetailRoom
import olmo.wellness.android.data.model.chat.room.User
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.domain.model.booking.ServiceBookingForCart
import olmo.wellness.android.webrtc.socket.SocketApi
import org.json.JSONObject
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RtcService @Inject constructor(
    private val socket: SocketApi,
) : CoroutineScope, RtcServiceApi {

    companion object {
        val TAG = RtcService::class.java.name
        val gson = Gson()
    }

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.IO

    init {
        launch {
            socket.messages.consumeEach {
                onMessageReceived(it)
            }
        }
    }

    private val _connectionState: MutableStateFlow<ConnectionState?> = MutableStateFlow(null)
    override var connectionState: StateFlow<ConnectionState?> = _connectionState
    private val _pingPong: MutableStateFlow<Long?> = MutableStateFlow(null)

    private val _userLeaveRoomLiveStream: MutableStateFlow<User?> = MutableStateFlow(null)
    private val _userJoinRoomLiveStream: MutableStateFlow<User?> = MutableStateFlow(null)
    private val _listMessageLiveStream: MutableStateFlow<List<ChatMessage>> =
        MutableStateFlow(emptyList())
    private val _chatMessageLiveStream: MutableStateFlow<ChatMessage?> = MutableStateFlow(null)
    private val _infoRoomLiveStream: MutableStateFlow<DetailRoom?> = MutableStateFlow(null)
    private val _reactionRoomLiveStream: MutableStateFlow<ReactionCount?> = MutableStateFlow(null)
    private val _reactionMessageRoomLiveStream: MutableStateFlow<ChatMessage?> =
        MutableStateFlow(null)
    private val _tipPackageLiveStream: MutableStateFlow<ChatMessage?> = MutableStateFlow(null)

    private val _userLeaveRoomPrivateChat: MutableStateFlow<User?> = MutableStateFlow(null)
    private val _userJoinRoomPrivateChat: MutableStateFlow<User?> = MutableStateFlow(null)
    private val _listMessagePrivateChat: MutableStateFlow<List<ChatMessage>> =
        MutableStateFlow(emptyList())
    private val _chatMessagePrivateChat: MutableStateFlow<ChatMessage?> = MutableStateFlow(null)
    private val _infoRoomPrivateChat: MutableStateFlow<DetailRoom?> = MutableStateFlow(null)
    private val _reactionRoomPrivateChat: MutableStateFlow<ReactionCount?> = MutableStateFlow(null)
    private val _reactionMessageRoomPrivateChat: MutableStateFlow<ChatMessage?> =
        MutableStateFlow(null)

    private val _servicesOfLiveStream: MutableStateFlow<List<ServiceBookingForCart>> =
        MutableStateFlow(emptyList())


    private fun clearDataLs() {
        _userLeaveRoomLiveStream.update { null }
        _userJoinRoomLiveStream.update { null }
        _listMessageLiveStream.update { emptyList() }
        _chatMessageLiveStream.update { null }
        _infoRoomLiveStream.update { null }
        _reactionRoomLiveStream.update { null }
        _reactionMessageRoomLiveStream.update { null }
    }

    private fun clearDataPC() {
        _userLeaveRoomPrivateChat.update { null }
        _userJoinRoomPrivateChat.update { null }
        _listMessagePrivateChat.update { emptyList() }
        _chatMessagePrivateChat.update { null }
        _infoRoomPrivateChat.update { null }
        _reactionRoomPrivateChat.update { null }
        _reactionMessageRoomPrivateChat.update { null }
    }


    override fun messagesLiveStream(): StateFlow<List<ChatMessage>> {
        return _listMessageLiveStream
    }

    override fun chatMessageLiveStream(): StateFlow<ChatMessage?> {
        return _chatMessageLiveStream
    }

    override fun reactionRoomLiveStream(): StateFlow<ReactionCount?> {
        return _reactionRoomLiveStream
    }

    override fun reactionMessageRoomLiveStream(): StateFlow<ChatMessage?> {
        return _reactionMessageRoomLiveStream
    }


    @OptIn(DelicateCoroutinesApi::class)
    private val onMessageReceived: (String) -> Unit = { text ->
        if (text.isNotEmpty()) {
            Log.d(TAG, "Receive - $text")
            val message = JSONObject(text)

            if (message.has(RtcConstant.TYPE) && message.has(RtcConstant.DATA)) {

                val type = message.getString(RtcConstant.TYPE)
                when (type) {
                    RtcConstant.PLAIN -> {
                        val body = JSONObject(message.getString(RtcConstant.DATA))
                        if (body.has(RtcConstant.MESSAGE)) {
                            val message = JSONObject(body.getString(RtcConstant.MESSAGE))
                            if (message.has(RtcConstant.CHANNEL)) {
                                when (message.getString(RtcConstant.CHANNEL)) {
                                    RtcConstant.INFO_USER -> {
                                        _connectionState.update {
                                            ConnectionState.CONNECTED
                                        }
//                                    clearDataLs()
                                    }
                                    RtcConstant.INFO_ROOM_LIVE_STEAM -> {
                                        if (_infoRoomLiveStream.value != null) {
                                            _infoRoomLiveStream.update { null }
                                        }
                                        val roomString =
                                            message.getJSONObject(RtcConstant.ROOM).toString()
                                        val room = gson.fromJson(roomString, DetailRoom::class.java)
                                        _infoRoomLiveStream.update { room }
                                    }
                                    RtcConstant.LOAD_MESSAGE_LIVE_STREAM -> {
                                        if (_listMessageLiveStream.value.isNotEmpty()) {
                                            _listMessageLiveStream.update { emptyList() }
                                        }
                                        val listMessageJson =
                                            message.getJSONArray(RtcConstant.MESSAGES)
                                        val messages = mutableListOf<ChatMessage>()
                                        for (i in 0 until listMessageJson.length()) {
                                            messages.add(
                                                gson.fromJson(
                                                    listMessageJson.get(i).toString(),
                                                    ChatMessage::class.java
                                                )
                                            )
                                        }
                                        messages.reverse()
                                        _listMessageLiveStream.update { messages }
                                    }
                                    RtcConstant.PRIVATE_CHAT_LIVE_STREAM -> {
                                        if (_chatMessageLiveStream.value != null) {
                                            _chatMessageLiveStream.update { null }
                                        }
                                        val chatMessageString =
                                            message.getJSONObject(RtcConstant.MESSAGE).toString()
                                        _chatMessageLiveStream.update {
                                            gson.fromJson(chatMessageString,
                                                ChatMessage::class.java)
                                        }
                                    }
                                    RtcConstant.JOIN_ROOM_LIVE_STREAM -> {
                                        if (_userJoinRoomLiveStream.value != null) {
                                            _userJoinRoomLiveStream.update { null }
                                        }
                                        val userString =
                                            message.getJSONObject(RtcConstant.USER).toString()
                                        _userJoinRoomLiveStream.update {
                                            (gson.fromJson(
                                                userString,
                                                User::class.java
                                            ))
                                        }
                                    }
                                    RtcConstant.UPDATE_REACTION_LIVE_STREAM -> {
                                        if (_reactionRoomLiveStream.value != null) {
                                            _reactionRoomLiveStream.update { null }
                                        }
                                        val countReaction =
                                            message.getInt(RtcConstant.COUNT_REACTION)
                                        _reactionRoomLiveStream.update {
                                            ReactionCount(
                                                countReaction = countReaction,
                                                countType = (if (countReaction > 0) CountType.UP else CountType.DOWN)
                                            )
                                        }
                                    }
                                    RtcConstant.SEND_TIP_PACKAGE_LIVESTREAM -> {
                                        if (_tipPackageLiveStream.value != null) {
                                            _tipPackageLiveStream.update { null }
                                        }
                                        val packageString =
                                            message.getJSONObject(RtcConstant.MESSAGE).toString()
                                        _tipPackageLiveStream.update {
                                            gson.fromJson(packageString, ChatMessage::class.java)
                                        }
                                    }
                                    RtcConstant.UPDATE_REACTION_MESSAGE_LIVE_STREAM -> {
                                        if (_reactionMessageRoomLiveStream.value != null) {
                                            _reactionMessageRoomLiveStream.update { null }
                                        }
                                        val messageString =
                                            message.getJSONObject(RtcConstant.MESSAGE).toString()
                                        val userId = message.getInt(RtcConstant.USER_ID)
                                        val message =
                                            gson.fromJson(messageString, ChatMessage::class.java)
                                                .apply {
                                                    userIdReaction = userId
                                                }
                                        _reactionMessageRoomLiveStream.update {
                                            message
                                        }
                                    }
                                    RtcConstant.LEAVE_ROOM_LIVE_STREAM -> {
                                        if (_userLeaveRoomLiveStream.value != null) {
                                            _userLeaveRoomLiveStream.update { null }
                                        }
                                        val userString =
                                            message.getJSONObject(RtcConstant.USER).toString()
                                        _userLeaveRoomLiveStream.update {
                                            (gson.fromJson(userString, User::class.java))
                                        }
                                    }
                                    RtcConstant.INFO_ROOM_CHAT -> {
                                        if (_infoRoomPrivateChat.value != null) {
                                            _infoRoomPrivateChat.update { null }
                                        }
                                        val roomString =
                                            message.getJSONObject(RtcConstant.ROOM).toString()
                                        val room = gson.fromJson(roomString, DetailRoom::class.java)
                                        _infoRoomPrivateChat.update { room }
                                    }
                                    RtcConstant.JOIN_ROOM_CHAT -> {
                                        if (_userJoinRoomPrivateChat.value != null) {
                                            _userJoinRoomPrivateChat.update { null }
                                        }
                                        val userString =
                                            message.getJSONObject(RtcConstant.USER).toString()
                                        _userJoinRoomPrivateChat.update {
                                            (gson.fromJson(
                                                userString,
                                                User::class.java
                                            ))
                                        }
                                    }
                                    RtcConstant.LOAD_MESSAGE_CHAT -> {
                                        if (_listMessagePrivateChat.value.isNotEmpty()) {
                                            _listMessagePrivateChat.update { emptyList() }
                                        }
                                        val listMessageJson =
                                            message.getJSONArray(RtcConstant.MESSAGES)
                                        val messages = mutableListOf<ChatMessage>()
                                        for (i in 0 until listMessageJson.length()) {
                                            messages.add(
                                                gson.fromJson(
                                                    listMessageJson.get(i).toString(),
                                                    ChatMessage::class.java
                                                )
                                            )
                                        }
                                        messages.reverse()
                                        _listMessagePrivateChat.update { messages }
                                    }
                                    RtcConstant.PRIVATE_CHAT -> {
                                        if (_chatMessagePrivateChat.value != null) {
                                            _chatMessagePrivateChat.update { null }
                                        }
                                        val chatMessageString =
                                            message.getJSONObject(RtcConstant.MESSAGE).toString()
                                        _chatMessagePrivateChat.update {
                                            gson.fromJson(
                                                chatMessageString,
                                                ChatMessage::class.java
                                            )
                                        }
                                    }
                                    RtcConstant.GET_SERVICE_OF_LIVESTREAM -> {
                                        if (_servicesOfLiveStream.value.isNotEmpty()) {
                                            _servicesOfLiveStream.update { emptyList() }
                                        }
                                        val servicesString = message.getJSONObject(RtcConstant.DATA)
                                            .getJSONArray(RtcConstant.RECORDS).toString()
                                        val servicesOfLiveStream = gson.fromJson(servicesString,
                                            Array<ServiceBookingForCart>::class.java).asList()
                                        _servicesOfLiveStream.update {
                                            servicesOfLiveStream
                                        }

                                    }
                                    RtcConstant.LEAVE_ROOM_CHAT -> {
                                        clearDataPC()
                                    }

                                }
                            }
                        }
                    }
                    RtcConstant.PING_PONG -> {
                        _pingPong.update { System.currentTimeMillis() }
                    }
                }

            }
        }
    }

    inline fun <reified T> fromJson(json: String): T {
        return Gson().fromJson(json, object : TypeToken<T>() {}.type)
    }

    override fun requestSession(token: String?) {
        socket.connect(token)
    }

    override fun sendJoinRoomLiveStream(event: EventRtc) {
        sendSocket(event = event)
    }

    override fun sendReactionLiveStream(event: EventRtc) {
        sendSocket(event = event)
    }

    override fun sendReactionMessageRoomLiveStream(event: EventRtc) {
        sendSocket(event = event)
    }

    override fun sendTipPackage(event: EventRtc) {
        sendSocket(event = event)
    }

    override fun roomDetailLiveStream(): StateFlow<DetailRoom?> {
        return _infoRoomLiveStream
    }


    private fun sendSocket(type: String = RtcConstant.PLAIN, event: EventRtc) {
        socket.sendMessage(type, JSONObject(Gson().toJson(event)))
    }

    private fun sendSocket(type: String = RtcConstant.PLAIN, event: Int) {
        socket.sendMessage(type, event)
    }

    override fun disconnectSession() {
        socket.disconnect()
        _connectionState.update { ConnectionState.DISCONNECTED }
    }

    override fun pingPong(): StateFlow<Long?> {
        return _pingPong
    }

    override fun sendPingPong() {
        sendSocket(type = RtcConstant.PING_PONG, event = 1)
    }

    override fun sendMessageLiveStream(event: EventRtc) {
        sendSocket(event = event)
    }

    override fun sendLeaveRoomLiveStream(event: EventRtc) {
        sendSocket(event = event)
        clearDataLs()
    }

    override fun sendLoadMoreLiveStream(event: EventRtc) {
        sendSocket(event = event)
    }

    override fun userJoinRoomLiveStream(): StateFlow<User?> {
        return _userJoinRoomLiveStream
    }

    override fun userLeaveRoomLiveStream(): StateFlow<User?> {
        return _userLeaveRoomLiveStream
    }

    override fun tipPackage(): StateFlow<ChatMessage?> {
        return _tipPackageLiveStream
    }

    override fun messagesPrivateChat(): StateFlow<List<ChatMessage>> {
        return _listMessagePrivateChat
    }

    override fun chatMessagePrivateChat(): StateFlow<ChatMessage?> {
        return _chatMessagePrivateChat
    }

    override fun reactionMessageRoomPrivateChat(): StateFlow<ChatMessage?> {
        return _reactionMessageRoomPrivateChat
    }

    override fun sendJoinRoomPrivateChat(event: EventRtc) {
        sendSocket(event = event)
    }

    override fun sendReactionMessageRoomPrivateChat(event: EventRtc) {
        sendSocket(event = event)
    }

    override fun getRoomDetailPrivateChat(): StateFlow<DetailRoom?> {
        return _infoRoomPrivateChat
    }

    override fun sendMessagePrivateChat(event: EventRtc) {
        sendSocket(event = event)
    }

    override fun sendLeaveRoomPrivateChat(event: EventRtc) {
        sendSocket(event = event)
    }

    override fun sendLoadMorePrivateChat(event: EventRtc) {
        sendSocket(event = event)
    }

    override fun userJoinRoomPrivateChat(): StateFlow<User?> {
        return _userJoinRoomPrivateChat
    }

    override fun userLeaveRoomPrivateChat(): StateFlow<User?> {
        return _userLeaveRoomPrivateChat
    }

    override fun sendBookmark(event: EventRtc) {
        sendSocket(event = event)
    }

    override fun sendAddService(event: EventRtc) {
        sendSocket(event= event)
    }

    override fun servicesOfLiveStream(): StateFlow<List<ServiceBookingForCart>> {
        return  _servicesOfLiveStream
    }

    override fun deleteServiceOfLiveStream(event: EventRtc) {
        sendSocket(event = event)
    }
}