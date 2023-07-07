package olmo.wellness.android.data.model.notification


import com.google.gson.annotations.SerializedName

data class DataNotification(
    @SerializedName("payload")
    val payload: PayloadNotification? = null,
    @SerializedName("type")
    val type: String? = null
)