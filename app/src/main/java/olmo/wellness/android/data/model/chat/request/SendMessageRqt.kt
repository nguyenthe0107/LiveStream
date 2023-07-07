package olmo.wellness.android.data.model.chat.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.data.model.chat.ChatMessage

data class SendMessageRqt(
    @SerializedName("message")
    @Expose
    val message: ChatMessage?,
)