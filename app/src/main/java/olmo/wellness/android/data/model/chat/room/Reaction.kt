package olmo.wellness.android.data.model.chat.room

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Reaction(
    @SerializedName("user_id")
    @Expose
    val userId: Int? = null,
    @SerializedName("reaction")
    @Expose
    val reaction: String? = null
)