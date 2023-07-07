package olmo.wellness.android.domain.model.booking

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PhotoBooking(
    @SerializedName("createdAt")
    @Expose
    val createdAt: Long?=null,
    @SerializedName("id")
    @Expose
    val id: Int?=null,
    @SerializedName("lastModified")
    @Expose
    val lastModified: Long?=null,
    @SerializedName("serviceId")
    @Expose
    val serviceId: Int?=null,
    @SerializedName("url")
    @Expose
    val url: String?=null
)