package olmo.wellness.android.domain.model.chat

data class ConversationInfo(
    val id: Int,
    val name : String,
    val avatar : String,
    val isOnline : Boolean=false,
    val isMute : Boolean=false,
    val lastDate : String,
    val lastMessage : String,
)