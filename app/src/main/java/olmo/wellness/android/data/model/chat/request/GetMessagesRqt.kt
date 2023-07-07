package olmo.wellness.android.data.model.chat.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetMessagesRqt(
    @SerializedName("indexMessageFirst")
    @Expose
    val indexMessageFirst: Any?,
    @SerializedName("roomId")
    @Expose
    val roomId: String?
)