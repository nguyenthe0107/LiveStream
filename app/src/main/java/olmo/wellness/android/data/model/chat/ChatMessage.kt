package olmo.wellness.android.data.model.chat


import androidx.compose.runtime.mutableStateListOf
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.data.model.chat.room.User

data class ChatMessage(
    @SerializedName("content")
    @Expose
    var content: String?,
    @SerializedName("createdAtTimestamp")
    @Expose
    val createdAtTimestamp: Long? = null,
    @SerializedName("_id")
    @Expose
    val id: String? = null,
    @SerializedName("reactions")
    @Expose
    var reactions: MutableList<Int?>? = mutableStateListOf(),
    @SerializedName("replyId")
    @Expose
    val replyId: String? = null,
    @SerializedName("roomId")
    @Expose
    val roomId: String? = null,
    @SerializedName("type")
    @Expose
    val type: String? = null,
    @SerializedName("userId")
    @Expose
    val userId: Int? = null,
    @SerializedName("user")
    @Expose
    var user: User? = null,
    @SerializedName("isHost")
    @Expose
    var isHost: Boolean = false,
    @SerializedName("object")
    @Expose
    var objectData: ChatObject? = null,
    @SerializedName("listChild")
    @Expose
    var listChild: MutableList<ChatMessage?>?=null,
    @SerializedName("idUserReaction") //id convert -> listReaction
    @Expose
    var userIdReaction: Int? = null,
    @SerializedName("change") //id convert -> listReaction
    @Expose
    var change: Long? = 0,
) {

}