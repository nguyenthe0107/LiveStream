package olmo.wellness.android.data.model.chat


import com.google.gson.annotations.SerializedName
import olmo.wellness.android.data.model.chat.room.DetailRoom
import olmo.wellness.android.data.model.chat.room.LastMessage
import olmo.wellness.android.data.model.chat.room.User

data class RoomListChatSingleDTO(
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("creatorId")
    val creatorId: Any?,
    @SerializedName("fileStorages")
    val fileStorages: List<Any?>?,
    @SerializedName("_id")
    val id: String?,
    @SerializedName("isGroup")
    val isGroup: Boolean?,
    @SerializedName("lastMessage")
    val lastMessage: LastMessage?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("totalMessage")
    val totalMessage: Int?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("users")
    val users: MutableList<User>?,
)

fun RoomListChatSingleDTO.toDetailRoom(): DetailRoom{
    return DetailRoom(createdAt =createdAt, creatorId = creatorId, fileStorages = fileStorages, id = id
    , isGroup = isGroup, lastMessage = lastMessage, name = name, totalMessage = totalMessage, type = type, updatedAt = updatedAt, users = users)
}

