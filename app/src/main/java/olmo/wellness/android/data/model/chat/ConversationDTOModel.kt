package olmo.wellness.android.data.model.chat

import olmo.wellness.android.domain.model.chat.ConversationInfo

data class ConversationInfoDTOModel(
    val id: Int,
    val name: String,
    val avatar: String,
    val isOnline: Boolean = false,
    val isMute: Boolean = false,
    val lastDate: String,
    val lastMessage : String,
)

fun ConversationInfoDTOModel.toConversationInfoInfoDomain(): ConversationInfo {
    return ConversationInfo(
        id, name, avatar, isOnline, isMute, lastDate,lastMessage
    )
}