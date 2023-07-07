package olmo.wellness.android.ui.livestream.chatlivestream.state

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import olmo.wellness.android.data.model.chat.*
import olmo.wellness.android.data.model.chat.room.DetailRoom
import olmo.wellness.android.data.model.chat.room.User
import olmo.wellness.android.sharedPrefs
import java.util.Date

class ChatLivestreamUiState(
    private var roomInfo: DetailRoom? = null,
    initialMessages: List<ChatMessage> = emptyList(),
    val isShowChild: Boolean = false // when livestream -> false, watch view => true
) {

    private val _message: MutableList<ChatMessage?> =
        mutableStateListOf(*initialMessages.toTypedArray())
    val messages: List<ChatMessage?> = _message

    private var hostRoomId: Int? = null


    private var _countHeart: MutableStateFlow<ReactionCount> = MutableStateFlow(ReactionCount(countReaction = 0, countType = CountType.NORMAL))

    private val _countComment: MutableStateFlow<Int?> = MutableStateFlow(0)

    private val _countDonation : MutableStateFlow<Int> = MutableStateFlow(0)
    private val _donation : MutableStateFlow<ChatObject?> = MutableStateFlow(null)

    private var _isLikeRoom: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var _userMap: MutableList<User> = mutableStateListOf(*emptyList<User>().toTypedArray())
    val userMap : List<User> = _userMap

    private var _countUser : MutableStateFlow<Int> = MutableStateFlow(0)

    private var _userChatPrivate : MutableStateFlow<User?> = MutableStateFlow(null)

    fun setHostRoom(hostId: Int?) {
        hostRoomId = hostId
    }

    fun addDonation(donation : ChatObject){
        _countDonation.value=_countDonation.value+1
        _donation.value= donation
    }

    fun addMessage(msg: ChatMessage) {
        if (!isShowChild) {
            addGroupMessage(msg)
        } else {
            val indexReply = _message.indexOfFirst { msg.replyId == it?.id }
            if (indexReply >= 0) {
                addChildMessage(indexReply, msg)
            } else {
                addGroupMessage(msg)
            }
        }

    }

    fun getRoomId() = roomInfo?.id

    @SuppressLint("SuspiciousIndentation")
    private fun addChildMessage(
        indexReply: Int,
        msg: ChatMessage
    ) {
        val index = _message[indexReply]?.listChild?.indexOfFirst { msg.id == it?.id }
        if (index == null || (index < 0)) {
            val item = _message[indexReply]
            if (item?.listChild != null && item.listChild?.isNotEmpty() == true) {
                _message[indexReply] = item.copy(
                    listChild = item.listChild?.map { it }?.toMutableList()?.apply {
                        add(0, updateInfoUseMessage(msg))
                    }
                )
            } else {
                _message[indexReply] = item?.copy(
                    listChild = mutableListOf<ChatMessage?>().apply {
                        add(0, updateInfoUseMessage(msg))
                    },
                )
            }

        }
    }

    private fun addGroupMessage(msg: ChatMessage) {
        val index = _message.indexOfFirst { msg.id == it?.id }
        if (index < 0) {
            _message.add(0, updateInfoUseMessage(msg))
            _countComment.value = _countComment.value?.plus(1)
        }
    }

    fun updateHeartReaction(reactionCount: ReactionCount) {
        if (reactionCount.countReaction > _countHeart.value.countReaction) {
            _countHeart.value = _countHeart.value.copy(
                countReaction = reactionCount.countReaction,
                countType = CountType.UP
            )
        } else if (reactionCount.countReaction < _countHeart.value.countReaction) {
            _countHeart.value = _countHeart.value.copy(
                countReaction = reactionCount.countReaction,
                countType = CountType.DOWN
            )
        }
    }

    fun getHeartReaction(): StateFlow<ReactionCount> = _countHeart

    fun getCountComment(): StateFlow<Int?> = _countComment

    fun getCountUser() : StateFlow<Int> =_countUser

    fun getUserChatPrivate() : StateFlow<User?> = _userChatPrivate

    fun getIsLikeRoom(): MutableStateFlow<Boolean> = _isLikeRoom

    fun getCountDonation(): StateFlow<Int?> =_countDonation

    fun getDonation(): StateFlow<ChatObject?> =_donation

    fun setLikeRoom(isLike: Boolean? = null) {
        if (isLike == true) {
            _isLikeRoom.value = true
        } else {
            val index= _userMap.indexOfFirst { it.id ==sharedPrefs.getUserInfoLocal().userId }
            if (index>=0){
                _isLikeRoom.value = _userMap[index].countReaction != null &&  _userMap[index].countReaction!! > 0
            }
        }
    }


    fun setCountHeartServer(count: Int) {
        _countHeart.value = _countHeart.value.copy(
            countReaction = count,
            countType = CountType.NORMAL
        )
    }

    fun updateUserJoinRoom(user: User) {
        val index = _userMap.indexOfFirst { it.id == user.id }
        if (index<0){
            _userMap.add(user)
            _countUser.value = _countUser.value+1
            createMessageUserJoinRoom(user)
        }
    }

    private fun createMessageUserJoinRoom(user: User) {
            val tempChat =
                ChatMessage(content = "Just joined", createdAtTimestamp = Date().time, user = user, type = MessageType.TEXT.value)
            _message.add(0, tempChat)
    }

    fun addListMessage(list: List<ChatMessage?>?) {
        if (list != null) {
            val tempList = list.map { updateInfoUseMessage(it) }.toMutableList()
            _message.addAll(tempList)
        } else {
            _message.clear()
        }
    }

    private fun updateInfoUseMessage(message: ChatMessage?): ChatMessage? {
        if (message?.listChild != null && message.listChild?.isEmpty() == false) {
            message.listChild?.reverse()
        }
        return message?.apply {
            isHost = checkHostRoom(message.userId)
        }
    }

    fun getTopIdMessage(): String? {
        return if (_message.size > 0) {
            _message[_message.size - 1]?.id
        } else null
    }

    fun setRoomInfo(_roomInfo: DetailRoom?) {
        roomInfo = _roomInfo
        _countComment.value = _roomInfo?.totalMessage
        roomInfo?.users?.forEach {
            _userMap.add(it)
        }
        _countUser.value= _roomInfo?.users?.size?:0
        if (_roomInfo?.users?.size==2){
            setUserPrivateChat()
        }
        val index= _userMap.indexOfFirst { sharedPrefs.getUserInfoLocal().userId== it.id }
        if (index <0) {
            val me = sharedPrefs.getUserInfoLocal()
            val user = User(
                id = me.userId,
                isOnline = true,
                isBan = false,
                isMuted = false,
                name = me.name,
                email = me.email,
                avatar = me.avatar)
            _userMap.add(user)
            _countUser.value=_countUser.value+1
        }
        setLikeRoom()
    }

    fun likeMessage(mesLike: ChatMessage?) {
        mesLike?.id?.let { msgId ->
            val index = _message.indexOfFirst { msgId == it?.id }
            if (index >= 0) {
                val item = _message[index]
                if (item?.reactions != null && item.reactions?.isNotEmpty() == true) {
                    _message[index] = item.copy(
                        reactions = item.reactions?.apply {
                            add(mesLike.userIdReaction)
                        }
                    )
                } else {
                    _message[index] = item?.copy(
                        reactions = mutableListOf<Int?>().apply {
                            add(mesLike.userIdReaction)
                        }
                    )
                }
            } else {
                _message.forEachIndexed { index, chatMessage ->
                    val position = chatMessage?.listChild?.indexOfFirst { it?.id == mesLike.id }
                    if (position != null && position >= 0) {
                        _message[index] = chatMessage.copy(
                            listChild = chatMessage.listChild?.map { it }?.toMutableList()?.apply {
                                this[position]?.reactions = mutableListOf<Int?>().apply {
                                    add(mesLike.userIdReaction)
                                }
                            },
                            change = System.currentTimeMillis()
                        )
                        return
                    }
                }
            }
        }
    }

    private fun checkHostRoom(userId: Int?): Boolean {
        return hostRoomId == userId
    }

    //get user private chat
    private fun setUserPrivateChat() {
        val meId = sharedPrefs.getUserInfoLocal().userId
        val index = _userMap.indexOfFirst { it.id != meId }
        if (index >= 0) {
            _userChatPrivate.value=_userMap[index]
        }
    }
}




