package olmo.wellness.android.data.model.chat.room

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DetailRoom(
    @SerializedName("_id")
    @Expose
    val id: String? = null,
    @SerializedName("totalMessage")
    @Expose
    val totalMessage: Int? = null,
    @SerializedName("lastMessage")
    @Expose
    val lastMessage: LastMessage? = null,
    @SerializedName("users")
    @Expose
    var users: MutableList<User>? = null,
    @SerializedName("countHeart")
    @Expose
    var countHeart : Int=0,
    @SerializedName("createdAt")
    val createdAt: String?= null,
    @SerializedName("creatorId")
    val creatorId: Any?= null,
    @SerializedName("fileStorages")
    val fileStorages: List<Any?>?= null,
    @SerializedName("isGroup")
    val isGroup: Boolean?= null,
    @SerializedName("name")
    val name: String?= null,
    @SerializedName("type")
    val type: String?= null,
    @SerializedName("updatedAt")
    val updatedAt: String?= null,
 ): Serializable
