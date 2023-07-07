package olmo.wellness.android.data.model.booking


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.domain.model.booking.PhotoBooking

data class PhotoDTO(
    @SerializedName("createdAt")
    @Expose
    val createdAt: Long?,
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("lastModified")
    @Expose
    val lastModified: Long?,
    @SerializedName("serviceId")
    @Expose
    val serviceId: Int?,
    @SerializedName("url")
    @Expose
    val url: String?
)


fun PhotoDTO.toPhotoBooking() : PhotoBooking{
    return PhotoBooking(createdAt, id, lastModified, serviceId, url)
}