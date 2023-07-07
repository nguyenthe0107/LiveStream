package olmo.wellness.android.ui.chat.private_chat.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import olmo.wellness.android.data.model.chat.ChatObject
import olmo.wellness.android.data.model.chat.FileObject
import olmo.wellness.android.data.model.chat.MessageType
import olmo.wellness.android.data.model.chat.room.DetailRoom
import olmo.wellness.android.domain.use_case.RoomChatUseCase
import olmo.wellness.android.domain.use_case.socket.private_chat.*
import olmo.wellness.android.sharedPrefs
import olmo.wellness.android.ui.base.BaseViewModel
import olmo.wellness.android.ui.chat.private_chat.event.PrivateChatEvent
import olmo.wellness.android.ui.chat.private_chat.event.PrivateChatState
import olmo.wellness.android.ui.livestream.chatlivestream.state.ChatLivestreamUiState
import olmo.wellness.android.webrtc.rtc.RtcUtils
import javax.inject.Inject

@HiltViewModel
class PrivateChatViewModel @Inject constructor(
    private val getRoomChatUseCase: RoomChatUseCase,
    private val sendRoomInfoPCUseCase: SendRoomInfoPCUseCase,
    private val getMessagesPCUseCase: GetMessagesPCUseCase,
    private val getChatMessagePCUseCase: GetChatMessagePCUseCase,
    private val sendMessagePCUseCase: SendMessagePCUseCase,
    private val sendLoadMoreMessagesPCUseCase: SendLoadMoreMessagesPCUseCase,
    private val getRoomInfoPCUseCase: GetRoomInfoPCUseCase,
    private val sendReactionMessageRoomPCUserCase: SendReactionMessageRoomPCUserCase,
    private val getReactionMessageRoomPCUserCase: SendReactionMessageRoomPCUserCase,
    private val sendLeaveRoomPCUseCase: SendLeaveRoomPCUseCase,
    private val getUserLeaveRoomPCUseCase: GetUserLeaveRoomPCUseCase,
) : BaseViewModel<PrivateChatState, PrivateChatEvent>() {
    private var roomIdPC: String? = null


    init {
        getRoomInfoPC()
        getMessagesRoomPC()
        receiveMessagePC()
        getUserLeaveRoomPC()

    }

    override fun initState(): PrivateChatState {
        return PrivateChatState()
    }

    override fun onTriggeredEvent(event: PrivateChatEvent) {
        when (event) {
            is PrivateChatEvent.ShowLoading -> {
                setState(
                    uiState.value.copy(
                        showLoading = event.isLoading
                    )
                )
            }
            is PrivateChatEvent.UpdateListChat -> {
                setState(uiState.value.copy(
                    showLoading = false,
                    roomStream = uiState.value.roomStream?.apply {
                        addListMessage(event.listChat)
                    }
                ))
            }
            is PrivateChatEvent.UpdateRoomDetail -> {
                setState(
                    uiState.value.copy(
                        showLoading = false,
                        roomStream = event.chatLivestreamUiState
                    )
                )
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getRoomInfoPC() {
        RtcUtils.getRoomInfoPC(getRoomInfoPCUseCase) { detail ->
            setRoomInfo(detail)
        }
    }

    private fun setRoomInfo(detail: DetailRoom) {
        if (roomIdPC != null) {
            val roomStream = ChatLivestreamUiState(isShowChild = true)
            roomStream.setRoomInfo(detail)
            triggerStateEvent(PrivateChatEvent.UpdateRoomDetail(roomStream))
            getListMessagePC(null, roomIdPC)
        }
    }

    private fun getListMessagePC(topMsgId: String?, _roomId: String?) {
        RtcUtils.sendLoadMorePC(
            topMsgId = topMsgId,
            roomId = _roomId,
            sendLoadMoreMessagesPCUseCase
        )
    }

    private fun getMessagesRoomPC() {
        RtcUtils.getMessagesRoomPC(
            getMessagesPCUseCase = getMessagesPCUseCase,
        ) {
            if (roomIdPC != null) {
                triggerStateEvent(PrivateChatEvent.UpdateListChat(it))
            }
        }
    }

    private fun receiveMessagePC() {
        RtcUtils.receiveMessagePC(getChatMessagePCUseCase) {
            if (roomIdPC == it.roomId) {
                val roomStream = uiState.value.roomStream?.apply {
                        addMessage(it)
                    }
                roomStream?.let { it1 -> triggerStateEvent(PrivateChatEvent.UpdateRoomDetail(it1) )}
            }
        }
    }

    private fun getUserLeaveRoomPC() {
        RtcUtils.getUserLeaveRoomPC(getUserLeaveRoomPCUseCase) {
            if (sharedPrefs.getUserInfoLocal().userId == it.id) {
            }
        }
    }

    private fun clearRoomPC() {
        RtcUtils.sendLeaveRoomPC(roomIdPC, sendLeaveRoomPCUseCase)
        clearState()
        roomIdPC = null
    }

    fun getRoomChatSingle(userId: Int?) {
        clearRoomPC()
        viewModelScope.launch {
            getRoomChatUseCase.getRoomChatSingle(userId).collectLatest { res ->
                res.onResultReceived(
                    onSuccess = {
                        roomIdPC = it?.id
                        it?.let { it1 -> setRoomInfo(it1) }
                    },
                    onError = {

                    }
                )
            }
        }
    }

    fun loadMorePC() {
        if (uiState.value.roomStream?.getTopIdMessage()?.isBlank() == false) {
            getListMessagePC(
                topMsgId = uiState.value.roomStream?.getTopIdMessage(),
                _roomId = roomIdPC
            )
        }
    }

    private fun sendJoinRoomPC(_roomId: String?) {
        if (_roomId != null) {
            roomIdPC = _roomId
            RtcUtils.sendJoinRoomPC(roomIdPC, sendRoomInfoPCUseCase)
        }
    }

    fun sendTextMessagePC(msg: String, replyId: String?) {
        RtcUtils.sendMessagePC(msg =msg, roomId = roomIdPC, replyId = replyId, sendMessagePCUseCase = sendMessagePCUseCase, type = MessageType.TEXT.value, chatObject = null)
    }

    fun sendImageMessagePC(images : List<String>,replyId: String?){
        val files= images.map { FileObject(url = it, type = MessageType.IMAGE.value) }
        val objectChat = ChatObject(files= files)
        RtcUtils.sendMessagePC(msg =null, roomId = roomIdPC, replyId = replyId, sendMessagePCUseCase = sendMessagePCUseCase, type = MessageType.FILE.value, chatObject = objectChat)

    }

}