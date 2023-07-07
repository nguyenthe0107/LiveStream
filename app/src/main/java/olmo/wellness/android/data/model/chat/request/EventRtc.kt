package olmo.wellness.android.data.model.chat.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EventRtc(
    @SerializedName("message")
    @Expose
    val message: MessageEvent
)