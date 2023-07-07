package olmo.wellness.android.data.model.chat.room

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LastMessage(
    @SerializedName("content")
    @Expose
    val content: String?,
    @SerializedName("createdAtTimestamp")
    @Expose
    val createdAtTimestamp: Long?,
    @SerializedName("_id")
    @Expose
    val id: String?,
    @SerializedName("reaction")
    @Expose
    val reaction: List<Any?>?,
    @SerializedName("replyId")
    @Expose
    val replyId: Any?,
    @SerializedName("roomId")
    @Expose
    val roomId: String?,
    @SerializedName("type")
    @Expose
    val type: String?,
    @SerializedName("userId")
    @Expose
    val userId: Int?
)