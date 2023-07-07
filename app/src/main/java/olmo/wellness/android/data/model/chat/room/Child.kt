package olmo.wellness.android.data.model.chat.room

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Child(
    @SerializedName("id")
    @Expose
    val id: Int? = null,
    @SerializedName("type")
    @Expose
    val type: String? = null,
    @SerializedName("content")
    @Expose
    val content: String? = null,
    @SerializedName("user_id")
    @Expose
    val userId: String? = null,
    @SerializedName("reaction")
    @Expose
    val reaction: List<Reaction>? = null
)