package olmo.wellness.android.webrtc.rtc

import android.util.Log
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import olmo.wellness.android.data.model.chat.*
import olmo.wellness.android.data.model.chat.request.*
import olmo.wellness.android.data.model.chat.room.DetailRoom
import olmo.wellness.android.data.model.chat.room.User
import olmo.wellness.android.domain.model.booking.ServiceBooking
import olmo.wellness.android.domain.model.booking.ServiceBookingForCart
import olmo.wellness.android.domain.use_case.socket.booking.DeleteServiceOfLiveStreamUseCase
import olmo.wellness.android.domain.use_case.socket.booking.GetServicesOfLiveStreamUseCase
import olmo.wellness.android.domain.use_case.socket.booking.SendAddServiceUseCase
import olmo.wellness.android.domain.use_case.socket.booking.SendBookmarkUseCase
import olmo.wellness.android.domain.use_case.socket.connection.*
import olmo.wellness.android.domain.use_case.socket.livestream.*
import olmo.wellness.android.domain.use_case.socket.private_chat.*
import olmo.wellness.android.sharedPrefs

object RtcUtils {

    fun connectionServer(token: String?, requestSessionUseCase: RequestSessionUseCase) {
        requestSessionUseCase.invoke(token)
    }

    fun disconnectServer(disconnectSessionUseCase: DisconnectSessionUseCase) {
        disconnectSessionUseCase.invoke(Unit)
    }

    fun sendPingPong(sendPingPongUseCase: SendPingPongUseCase) {
        sendPingPongUseCase.invoke(Unit)
    }

    fun getConnection(
        getConnectionStateUseCase: GetConnectionStateUseCase,
        onSuccess: (ConnectionState) -> Unit,
    ) {
        getConnectionStateUseCase.invoke(Unit) { data ->
            data.fold({ exception ->
            }, { connection ->
                GlobalScope.launch {
                    connection.collect {
                        it?.let { it1 -> onSuccess.invoke(it1) }
                    }
                }
            })
        }
    }

    fun getPingPong(getPingPongUseCase: GetPingPongUseCase, onSuccess: (Long) -> Unit) {
        getPingPongUseCase.invoke(Unit) { data ->
            data.fold({ exception -> }, { ping ->
                GlobalScope.launch {
                    ping.collect { pong ->
                        pong?.let { it1 -> onSuccess.invoke(it1) }
                    }
                }
            })
        }
    }

    fun sendJoinRoomPC(
        roomId: String?,
        sendRoomInfoPCUseCase: SendRoomInfoPCUseCase,
    ) {
        roomId?.let {
            val payload = PayLoad(it)
            val messageEvent =
                MessageEvent(event = RtcConstant.JOIN_ROOM_CHAT, payload = payload)
            val eventRtc = EventRtc(messageEvent)
            sendRoomInfoPCUseCase.invoke(eventRtc)
        }
    }

    fun sendReactionMessageRoomPC(
        roomId: String?,
        _messageId: String?,
        _isReaction: Boolean = true,
        sendReactionMessageRoomPCUserCase: SendReactionMessageRoomPCUserCase,
    ) {
//        roomId?.let {
//            val payLoad = PayLoad(it).apply {
//                messageId = _messageId
//                isReaction = _isReaction
//            }
//            val messageEvent = MessageEvent(
//                event = RtcConstant.UPDATE_REACTION_MESSAGE_LIVE_STREAM,
//                payload = payLoad
//            )
//            val eventRtc = EventRtc(messageEvent)
//            sendReactionMessageRoomPCUserCase.invoke(eventRtc)
//        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    fun getRoomInfoPC(getRoomInfoPCUseCase: GetRoomInfoPCUseCase, onSuccess: (DetailRoom) -> Unit) {
        getRoomInfoPCUseCase.invoke(Unit) { data ->
            data.fold({ exception ->
            }, { room ->
                GlobalScope.launch {
                    room.collect { detail ->
                        detail?.let { it1 -> onSuccess.invoke(it1) }
                    }
                }
            })
        }
    }

    fun sendLoadMorePC(
        topMsgId: String?,
        roomId: String?,
        sendLoadMoreMessagesPCUseCase: SendLoadMoreMessagesPCUseCase,
    ) {
        var indexMessageFirst: Any
        indexMessageFirst = if (!topMsgId.isNullOrBlank()) topMsgId else 0
        val rqt = GetMessagesRqt(
            roomId = roomId,
            indexMessageFirst = indexMessageFirst
        )
        val messageEvent =
            MessageEvent(event = RtcConstant.LOAD_MESSAGE_CHAT, payload = rqt)
        val eventRtc = EventRtc(messageEvent)
        sendLoadMoreMessagesPCUseCase.invoke(eventRtc)
    }


    @OptIn(DelicateCoroutinesApi::class)
    fun getMessagesRoomPC(
        getMessagesPCUseCase: GetMessagesPCUseCase,
        listMessages: (List<ChatMessage>) -> Unit,
    ) {
        getMessagesPCUseCase.invoke(Unit) { data ->
            data.fold({ exception ->
            }, { msgs ->
                GlobalScope.launch {
                    msgs.collect { mes ->
                        if (mes.isNotEmpty()) {
                            listMessages.invoke(mes)
                        }
                    }
                }

            })
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun receiveMessagePC(
        getChatMessagePCUseCase: GetChatMessagePCUseCase,
        newMessage: (ChatMessage) -> Unit,
    ) {
        getChatMessagePCUseCase.invoke(Unit) { data ->
            data.fold({ exception ->
            }, { msg ->
                GlobalScope.launch {
                    msg.collect() {
                        it?.let { it1 -> newMessage.invoke(it1) }
                    }

                }
            })
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun newUserJoinRoomPC(
        getUserJoinRoom: GetUserJoinRoomPCUseCase,
        onNewUser: (User) -> Unit,
    ) {
        getUserJoinRoom.invoke(Unit) { data ->
            data.fold({
            }, { user ->
                GlobalScope.launch {
                    user.collect() {
                        it?.let { it1 ->
                            onNewUser(it1)
                        }
                    }
                }
            })

        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    fun getUserLeaveRoomPC(
        getUserLeaveRoomPCUseCase: GetUserLeaveRoomPCUseCase,
        onLeaveUser: (User) -> Unit,
    ) {
        getUserLeaveRoomPCUseCase.invoke(Unit) { data ->
            data.fold({
            }, { user ->
                GlobalScope.launch {
                    user.collect {
                        it?.let { it1 -> onLeaveUser.invoke(it1) }
                    }
                }
            })
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getReactionMessageRoomPC(
        getReactionMessageRoomPCUserCase: GetReactionMessageRoomPCUserCase,
        _reactionMessage: (ChatMessage) -> Unit,
    ) {
        getReactionMessageRoomPCUserCase.invoke(Unit) { date ->
            date.fold({
            }, { reactionMessage ->
                GlobalScope.launch {
                    reactionMessage.collect {
                        it?.let { it1 -> _reactionMessage.invoke(it1) }
                    }
                }

            })
        }
    }

    fun sendLeaveRoomPC(roomId: String?, sendLeaveRoomPCUseCase: SendLeaveRoomPCUseCase) {
        roomId?.let {
            val payload = PayLoad(it)
            val messageEvent =
                MessageEvent(event = RtcConstant.LEAVE_ROOM_CHAT, payload = payload)
            val eventRtc = EventRtc(messageEvent)
            sendLeaveRoomPCUseCase.invoke(eventRtc)
        }
    }


    fun sendMessagePC(
        msg: String?,
        roomId: String?,
        type: String,
        replyId: String?,
        chatObject: ChatObject?,
        sendMessagePCUseCase: SendMessagePCUseCase,
    ) {
        val message = ChatMessage(
            type = type,
            userId = sharedPrefs.getUserInfoLocal().userId,
            content = msg,
            roomId = roomId,
            replyId = replyId,
            objectData = chatObject
        )
        val messageEvent = MessageEvent(
            event = RtcConstant.PRIVATE_CHAT,
            payload = SendMessageRqt(message = message)
        )
        sendMessagePCUseCase.invoke(EventRtc(messageEvent))
    }


    fun sendJoinRoomLS(
        roomId: String?,
        sendRoomInfoLSUseCase: SendRoomInfoLSUseCase,
    ) {
        roomId?.let {
            val payload = PayLoad(it)
            val messageEvent =
                MessageEvent(event = RtcConstant.JOIN_ROOM_LIVE_STREAM, payload = payload)
            val eventRtc = EventRtc(messageEvent)
            sendRoomInfoLSUseCase.invoke(eventRtc)
        }
    }

    fun sendReactionMessageRoomLS(
        roomId: String?,
        _messageId: String?,
        _isReaction: Boolean = true,
        sendReactionMessageRoomLSUserCase: SendReactionMessageRoomLSUserCase,
    ) {
        roomId?.let {
            val payLoad = PayLoad(it).apply {
                messageId = _messageId
                isReaction = _isReaction
            }
            val messageEvent = MessageEvent(
                event = RtcConstant.UPDATE_REACTION_MESSAGE_LIVE_STREAM,
                payload = payLoad
            )
            val eventRtc = EventRtc(messageEvent)
            sendReactionMessageRoomLSUserCase.invoke(eventRtc)
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    fun getRoomInfoLS(getRoomInfoLSUseCase: GetRoomInfoLSUseCase, onSuccess: (DetailRoom) -> Unit) {
        getRoomInfoLSUseCase.invoke(Unit) { data ->
            data.fold({ exception ->
                Log.d(RtcService.TAG, "Error")
            }, { room ->
                GlobalScope.launch(Dispatchers.Main) {
                    room.collect { detail ->
                        detail?.let { it1 ->
                            onSuccess.invoke(it1)
                        }
                    }
                }
            })
        }
    }

    fun sendLoadMoreLS(
        topMsgId: String?,
        roomId: String?,
        sendLoadMoreMessagesLSUseCase: SendLoadMoreMessagesLSUseCase,
    ) {
        var indexMessageFirst: Any
        indexMessageFirst = if (!topMsgId.isNullOrBlank()) topMsgId else 0
        val rqt = GetMessagesRqt(
            roomId = roomId,
            indexMessageFirst = indexMessageFirst
        )
        val messageEvent =
            MessageEvent(event = RtcConstant.LOAD_MESSAGE_LIVE_STREAM, payload = rqt)
        val eventRtc = EventRtc(messageEvent)
        sendLoadMoreMessagesLSUseCase.invoke(eventRtc)
    }


    @OptIn(DelicateCoroutinesApi::class)
    fun getMessagesRoomLS(
        getMessagesLSUseCase: GetMessagesLSUseCase,
        listMessages: (List<ChatMessage>) -> Unit,
    ) {
        getMessagesLSUseCase.invoke(Unit) { data ->
            data.fold({ exception ->
            }, { msgs ->
                GlobalScope.launch {
                    msgs.collect { mes ->
                        if (mes.isNotEmpty()) {
                            listMessages.invoke(mes)
                        }
                    }
                }

            })
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun receiveMessageLS(
        getChatMessageLSUseCase: GetChatMessageLSUseCase,
        newMessage: (ChatMessage) -> Unit,
    ) {
        getChatMessageLSUseCase.invoke(Unit) { data ->
            data.fold({ exception ->
            }, { msg ->
                GlobalScope.launch {
                    msg.collect() {
                        it?.let { it1 -> newMessage.invoke(it1) }
                    }

                }
            })
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun newUserJoinRoomLS(
        getUserJoinRoom: GetUserJoinRoomLSUseCase,
        onNewUser: (User) -> Unit,
    ) {
        getUserJoinRoom.invoke(Unit) { data ->
            data.fold({
            }, { user ->
                GlobalScope.launch(Dispatchers.Main) {
                    user.collect() {
                        it?.let { it1 ->
                            onNewUser(it1)
                        }
                    }
                }
            })

        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    fun getUserLeaveRoomLS(
        getUserLeaveRoomLSUseCase: GetUserLeaveRoomLSUseCase,
        onLeaveUser: (User) -> Unit,
    ) {
        getUserLeaveRoomLSUseCase.invoke(Unit) { data ->
            data.fold({
            }, { user ->
                GlobalScope.launch {
                    user.collect {
                        it?.let { it1 -> onLeaveUser.invoke(it1) }
                    }
                }
            })
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getReactionRoomLS(
        getReactionRoomLSUserCase: GetReactionRoomLSUserCase,
        _reaction: (ReactionCount) -> Unit,
    ) {
        getReactionRoomLSUserCase.invoke(Unit) { data ->
            data.fold({
            }, { reaction ->
                GlobalScope.launch {
                    reaction.collect {
                        it?.let { it1 -> _reaction.invoke(it1) }
                    }
                }
            })

        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getReactionMessageRoomLS(
        getReactionMessageRoomLSUserCase: GetReactionMessageRoomLSUserCase,
        _reactionMessage: (ChatMessage) -> Unit,
    ) {
        getReactionMessageRoomLSUserCase.invoke(Unit) { data ->
            data.fold({
            }, { reactionMessage ->
                GlobalScope.launch(Dispatchers.Main) {
                    reactionMessage.collect {
                        it?.let { it1 -> _reactionMessage.invoke(it1) }
                    }
                }

            })
        }
    }

    fun getTipPackage(
        getTipPackageUseCase: GetTipPackageUseCase,
        _tipPackage: (ChatMessage) -> Unit,
    ) {
        getTipPackageUseCase.invoke(Unit) { data ->
            data.fold({

            }, { tip ->
                GlobalScope.launch(Dispatchers.Main) {
                    tip.collect {
                        it?.let { it1 -> _tipPackage.invoke(it1) }
                    }
                }
            })
        }
    }


    fun sendLeaveRoomLS(roomId: String?, sendLeaveRoomLSUseCase: SendLeaveRoomLSUseCase) {
        roomId?.let {
            val payload = PayLoad(it)
            val messageEvent =
                MessageEvent(event = RtcConstant.LEAVE_ROOM_LIVE_STREAM, payload = payload)
            val eventRtc = EventRtc(messageEvent)
            sendLeaveRoomLSUseCase.invoke(eventRtc)
        }
    }

    fun sendReactionRoomLS(
        roomId: String?,
        sendReactionRoomLSUserCase: SendReactionRoomLSUserCase,
    ) {
        roomId?.let {
            val payload = PayLoad(roomId = it, countReaction = 1)
            val messageEvent =
                MessageEvent(event = RtcConstant.UPDATE_REACTION_LIVE_STREAM, payload = payload)
            val eventRtc = EventRtc(messageEvent)
            sendReactionRoomLSUserCase.invoke(eventRtc)
        }
    }

    fun sendMessageLS(
        msg: String, roomId: String?, replyId: String?, sendMessageLSUseCase: SendMessageLSUseCase,
    ) {
        if (msg.isNotBlank()) {
            val message = ChatMessage(
                type = MessageType.TEXT.value,
                userId = sharedPrefs.getUserInfoLocal().userId,
                content = msg,
                roomId = roomId,
                replyId = replyId
            )
            val messageEvent = MessageEvent(
                event = RtcConstant.PRIVATE_CHAT_LIVE_STREAM,
                payload = SendMessageRqt(message = message)
            )
            sendMessageLSUseCase.invoke(EventRtc(messageEvent))
        }
    }


    fun sendTipPackage(
        roomId: String?,
        tipPackageId: Int?,
        sendTipPackageUseCase: SendTipPackageUseCase,
    ) {
        if (roomId != null) {
            val payLoad = PayLoad(roomId = roomId, tipPackageId = tipPackageId)
            val tipEvent =
                MessageEvent(event = RtcConstant.SEND_TIP_PACKAGE_LIVESTREAM, payload = payLoad)
            val eventRtc = EventRtc(tipEvent)
            sendTipPackageUseCase.invoke(eventRtc)
        }
    }


    fun sendBookMark(
        roomId: String?,
        liveStreamId: Int?,
        serviceId: Int?,
        sendBookmarkUseCase: SendBookmarkUseCase,
    ) {
        if (roomId != null && liveStreamId != null && serviceId != null) {
            val payload =
                PayLoad(roomId = roomId, livestreamId = liveStreamId, serviceId = serviceId)
            val bookMarkEvent =
                MessageEvent(event = RtcConstant.BOOK_MARK_SERVICE_OF_LIVESTREAM, payload = payload)
            val eventRtc = EventRtc(bookMarkEvent)
            sendBookmarkUseCase.invoke(eventRtc)
        }
    }

    fun addServiceLivestream(
        roomId: String?,
        liveStreamId: Int?,
        serviceList: List<ServiceBooking>?,
        addServiceUseCase: SendAddServiceUseCase
    ) {
        if (roomId != null && liveStreamId != null && serviceList?.isNotEmpty()==true) {
            val services= mutableListOf<ServiceRtc>()
            for (i in serviceList.indices){
                services.add(ServiceRtc(bookmark = (i==0), serviceId = serviceList[i].id, index = i))
            }
            val payload =
                PayLoad(roomId = roomId, livestreamId = liveStreamId, services = services)
            val addServiceEvent =
                MessageEvent(event = RtcConstant.ADD_SERVICE_TO_LIVESTREAM, payload = payload)
            val eventRtc = EventRtc(addServiceEvent)
            addServiceUseCase.invoke(eventRtc)
        }
    }

    fun deleteServiceOfLiveStream(
        roomId: String?,
        liveStreamId: Int?,
        serviceId: Int?, deleteServiceOfLiveStreamUseCase: DeleteServiceOfLiveStreamUseCase,
    ) {
        if (roomId != null && liveStreamId != null && serviceId != null) {
            val payload =
                PayLoad(roomId = roomId, livestreamId = liveStreamId, serviceId = serviceId)
            val deleteEvent =
                MessageEvent(event = RtcConstant.DELETE_SERVICE_OF_LIVESTREAM, payload = payload)
            val eventRtc = EventRtc(deleteEvent)
            deleteServiceOfLiveStreamUseCase.invoke(eventRtc)
        }
    }

    fun getServiceOfLiveStream(
        getServicesOfLiveStreamUseCase: GetServicesOfLiveStreamUseCase,
        servicesOfLiveStream: (List<ServiceBookingForCart>) -> Unit,
    ) {
        getServicesOfLiveStreamUseCase.invoke(Unit) { data ->
            data.fold({ exception -> }, { services ->
                GlobalScope.launch {
                    services.collect { service ->
                        if (service.isNotEmpty()) {
                            servicesOfLiveStream.invoke(service)
                        }
                    }
                }
            })
        }
    }

}