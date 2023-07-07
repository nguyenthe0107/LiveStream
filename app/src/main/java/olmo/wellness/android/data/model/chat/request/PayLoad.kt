package olmo.wellness.android.data.model.chat.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.data.model.chat.ServiceRtc

data class PayLoad(
    @SerializedName("roomId")
    @Expose
    val roomId: String,
    @SerializedName("countReaction")
    @Expose
    val countReaction: Int? = null,
    @SerializedName("isReaction")
    @Expose
    var isReaction: Boolean? = null,
    @SerializedName("messageId")
    @Expose
    var messageId: String? = null,
    @SerializedName("tipPackageId")
    @Expose
    var tipPackageId: Int? = null,
    @SerializedName("livestreamId")
    @Expose
    var livestreamId: Int? = null,
    @SerializedName("serviceId")
    @Expose
    var serviceId: Int? = null,
    @SerializedName("services")
    @Expose
    var services: List<ServiceRtc>? = null,
)