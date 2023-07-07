package olmo.wellness.android.ui.livestream.chatlivestream.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import olmo.wellness.android.data.model.chat.ChatMessage
import olmo.wellness.android.domain.use_case.socket.livestream.*
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.base.BaseViewModel
import olmo.wellness.android.ui.livestream.chatlivestream.event.ChatLivestreamEvent
import olmo.wellness.android.ui.livestream.chatlivestream.event.ChatLivestreamState
import olmo.wellness.android.ui.livestream.chatlivestream.state.ChatLivestreamUiState
import olmo.wellness.android.webrtc.rtc.RtcUtils
import javax.inject.Inject

@HiltViewModel
class ChatLivestreamViewModel @Inject constructor(
    private val sendRoomInfoLSUseCase: SendRoomInfoLSUseCase,
    private val getMessagesLSUseCase: GetMessagesLSUseCase,
    private val sendMessageLSUseCase: SendMessageLSUseCase,
    private val getChatMessageLSUseCase: GetChatMessageLSUseCase,
    private val getUserJoinRoom: GetUserJoinRoomLSUseCase,
    private val getRoomInfoLSUseCase: GetRoomInfoLSUseCase,
    private val sendLeaveRoomLSUseCase: SendLeaveRoomLSUseCase,
    private val sendGetListMessagesUseCase: SendLoadMoreMessagesLSUseCase,
    private val getUserLeaveRoomLSUseCase: GetUserLeaveRoomLSUseCase,
    private val sendReactionRoomLSUserCase: SendReactionRoomLSUserCase,
    private val getReactionRoomLSUserCase: GetReactionRoomLSUserCase,
    private val sendTipPackageUseCase: SendTipPackageUseCase,
    private val getTipPackageUseCase: GetTipPackageUseCase,
    private val sendReactionMessageRoomLSUserCase: SendReactionMessageRoomLSUserCase,
    private val getReactionMessageRoomLSUserCase: GetReactionMessageRoomLSUserCase,
) :
    BaseViewModel<ChatLivestreamState, ChatLivestreamEvent>() {

    private var roomIdLS: String? = null
    private var hostRoomId: Int? = null
    private var heartCountServer: Int = 0

    private var isShowChild = false

    init {
        getRoomInfoLS()
        getMessagesRoomLS()
        receiveMessageLS()
        getUerJoinRoomLS()
        getUserLeaveRoomLS()
        getReactionRoomLS()
        getReactionMessageRoomLS()
        getTipPackageRoomLS()
    }

    override fun initState(): ChatLivestreamState {
        return ChatLivestreamState()
    }

    override fun onTriggeredEvent(event: ChatLivestreamEvent) {
        when (event) {
            is ChatLivestreamEvent.ShowLoading -> {
                setState(
                    uiState.value.copy(
                        showLoading = event.isLoading
                    )
                )
            }
            is ChatLivestreamEvent.UpdateRoomDetail -> {
                setState(
                    uiState.value.copy(
                        showLoading = false,
                        roomStream = event.chatLivestreamUiState
                    )
                )
            }
            is ChatLivestreamEvent.UpdateListChat -> {
                setState(uiState.value.copy(
                    showLoading = false,
                    roomStream = uiState.value.roomStream?.apply {
                        addListMessage(event.listChat)
                    }
                ))
            }
            is ChatLivestreamEvent.UpdateHeatCount -> {
                setState(uiState.value.copy(
                    roomStream = uiState.value.roomStream?.apply {
                        updateHeartReaction(event.reactionCount)
                    }
                ))
            }
            is ChatLivestreamEvent.UpdateUserJoinRoom -> {
                setState(uiState.value.copy(
                    roomStream = uiState.value.roomStream?.apply {
                        updateUserJoinRoom(event.user)
                    }
                ))
            }
            is ChatLivestreamEvent.UpdateIsComment -> {
                setState(
                    uiState.value.copy(
                        isComment = event.isComment
                    )
                )
            }
        }
    }

    private fun getTipPackageRoomLS() {
        RtcUtils.getTipPackage(getTipPackageUseCase = getTipPackageUseCase) {
            if (roomIdLS == it.roomId) {
                val roomStreamLS = uiState.value.roomStream?.apply {
                    addMessage(it)
                    it.objectData?.let { it1 -> addDonation(it1) }
                }
                triggerStateEvent(ChatLivestreamEvent.UpdateRoomDetail(roomStreamLS))
            }
        }
    }

    private fun getRoomInfoLS() {
        RtcUtils.getRoomInfoLS(getRoomInfoLSUseCase = getRoomInfoLSUseCase) {
            if (roomIdLS == it.id) {
                val roomState = ChatLivestreamUiState(isShowChild = isShowChild).apply {
                    setHostRoom(hostRoomId)
                    setRoomInfo(it)
                    setCountHeartServer(heartCountServer)
                }
                triggerStateEvent(ChatLivestreamEvent.UpdateIsComment(true))
                triggerStateEvent(ChatLivestreamEvent.UpdateRoomDetail(roomState))
                getListMessageLS(null, roomIdLS)

            }
        }
    }

    private fun getMessagesRoomLS() {
        RtcUtils.getMessagesRoomLS(
            getMessagesLSUseCase = getMessagesLSUseCase
        ) {
            if (roomIdLS == it[0].roomId) {
                triggerStateEvent(ChatLivestreamEvent.UpdateListChat(it))
            }
        }
    }

    private fun getListMessageLS(topMsgId: String?, _roomId: String?) {
        RtcUtils.sendLoadMoreLS(topMsgId = topMsgId, roomId = _roomId, sendGetListMessagesUseCase)
    }

    private fun receiveMessageLS() {
        RtcUtils.receiveMessageLS(getChatMessageLSUseCase) {
            if (roomIdLS == it.roomId) {
                val roomStreamLS = uiState.value.roomStream?.apply {
                    addMessage(it)
                }
                triggerStateEvent(ChatLivestreamEvent.UpdateRoomDetail(roomStreamLS))
            }
        }
    }

    fun loadMoreLS() {
        if (uiState.value.roomStream?.getTopIdMessage()?.isBlank() == false) {
            getListMessageLS(
                topMsgId = uiState.value.roomStream?.getTopIdMessage(),
                _roomId = roomIdLS
            )
        }
    }

    private fun updateReactionMessageLS(mesLike: ChatMessage?) {
        val roomStreamLS = uiState.value.roomStream?.apply {
            likeMessage(mesLike)
        }
        triggerStateEvent(ChatLivestreamEvent.UpdateRoomDetail(roomStreamLS))
    }


    private fun getUerJoinRoomLS() {
        RtcUtils.newUserJoinRoomLS(getUserJoinRoom) { user ->
            if (roomIdLS != null) {
                val roomStreamLS = uiState.value.roomStream?.apply {
                    updateUserJoinRoom(user)
                }
                triggerStateEvent(ChatLivestreamEvent.UpdateRoomDetail(roomStreamLS))
            }
        }
    }

    fun sendMessageLS(msg: String, replyId: String?) {
        RtcUtils.sendMessageLS(msg, roomIdLS, replyId, sendMessageLSUseCase)
    }




    private fun getUserLeaveRoomLS() {
        RtcUtils.getUserLeaveRoomLS(getUserLeaveRoomLSUseCase) {
            if (sharedPrefs.getUserInfoLocal().userId == it.id) {
            }
        }
    }


    private fun getReactionRoomLS() {
        RtcUtils.getReactionRoomLS(getReactionRoomLSUserCase) {
            if (roomIdLS != null) {
                triggerStateEvent(ChatLivestreamEvent.UpdateHeatCount(it))
            }
        }
    }

    private fun getReactionMessageRoomLS() {
        RtcUtils.getReactionMessageRoomLS(getReactionMessageRoomLSUserCase) {
            if (roomIdLS == it.roomId) {
                updateReactionMessageLS(it)
            }
        }
    }

    fun clearRoomLS(isClearId: Boolean = true) {
        RtcUtils.sendLeaveRoomLS(roomIdLS, sendLeaveRoomLSUseCase)
        if (isClearId) {
            roomIdLS = null
            hostRoomId = null
        }
        triggerStateEvent(ChatLivestreamEvent.UpdateRoomDetail(null))
        heartCountServer = 0
    }


    fun sendJoinRoomLS(
        _roomId: String?,
        _hostRoomId: Int? = null,
        _isShowChild: Boolean = false,
        _heartCountServer: Int? = 0
    ) {
        clearRoomLS()
        isShowChild = _isShowChild
        if (roomIdLS != _roomId && _roomId != null) {
            if (roomIdLS != null) {
                roomIdLS = _roomId
                hostRoomId = _hostRoomId
                clearRoomLS(false)
                if (_heartCountServer != null) {
                    heartCountServer = _heartCountServer
                }
                RtcUtils.sendJoinRoomLS(roomIdLS, sendRoomInfoLSUseCase)
            } else {
                roomIdLS = _roomId
                hostRoomId = _hostRoomId
                if (_heartCountServer != null) {
                    heartCountServer = _heartCountServer
                }
                RtcUtils.sendJoinRoomLS(roomIdLS, sendRoomInfoLSUseCase)
            }
        }
    }

    fun sendReactionRoom() {
        RtcUtils.sendReactionRoomLS(roomId = roomIdLS, sendReactionRoomLSUserCase)
    }

    fun sendTipPackage(
        tipPackageId: Int?,
    ) {
        RtcUtils.sendTipPackage(
            roomId = roomIdLS, tipPackageId, sendTipPackageUseCase
        )
    }

    fun sendReactionMessageRoomLS(messageId: String?) {
        RtcUtils.sendReactionMessageRoomLS(
            roomId = roomIdLS,
            messageId,
            true,
            sendReactionMessageRoomLSUserCase
        )
    }

    fun setIsComment(show: Boolean) {
        triggerStateEvent(ChatLivestreamEvent.UpdateIsComment(show))
    }

}