package olmo.wellness.android.data.model.notification


import com.google.gson.annotations.SerializedName

data class LivestreamNotification(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("roomChatId")
    val roomChatId: String? = null,
    @SerializedName("startTime")
    val startTime: Long? = null,
    @SerializedName("title")
    val title: String? = null
)