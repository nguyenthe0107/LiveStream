package olmo.wellness.android.data.model.chat

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.data.model.chat.room.DetailRoom
import olmo.wellness.android.data.model.chat.room.LastMessage
import olmo.wellness.android.data.model.chat.room.User

data class RoomChatSingleDTO(
    @Expose
    @SerializedName("_id")
    val _id: String? = null,
    @Expose
    @SerializedName("users")
    val users: MutableList<User>? = null,
    @Expose
    @SerializedName("totalMessage")
    val totalMessage: Int? = null,
    @SerializedName("lastMessage")
    @Expose
    val lastMessage: LastMessage? = null,
)

fun RoomChatSingleDTO.toRoomChatSingleDomain(): DetailRoom {
    return DetailRoom(id =_id, users = users, totalMessage = totalMessage, lastMessage = lastMessage)
}