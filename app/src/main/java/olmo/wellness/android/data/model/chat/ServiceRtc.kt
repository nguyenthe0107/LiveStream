package olmo.wellness.android.data.model.chat


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ServiceRtc(
    @SerializedName("bookmark")
    @Expose
    val bookmark: Boolean?,
    @SerializedName("index")
    @Expose
    val index: Int?,
    @SerializedName("serviceId")
    @Expose
    val serviceId: Int?,
)