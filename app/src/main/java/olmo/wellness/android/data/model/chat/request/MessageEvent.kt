package olmo.wellness.android.data.model.chat.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.data.model.chat.request.PayLoad

data class MessageEvent(
    @SerializedName("event")
    @Expose
    val event: String,
    @SerializedName("payload")
    @Expose
    val payload: Any?=null
)