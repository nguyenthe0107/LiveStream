package olmo.wellness.android.data.model.booking

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.domain.model.booking.CartBooking

data class CartBookingDTO(
    @SerializedName("createdAt")
    @Expose
    val createdAt: Long?,
    @SerializedName("id")
    @Expose
    val id: Int?,
    @SerializedName("lastModified")
    @Expose
    val lastModified: Long?,
    @SerializedName("locationId")
    @Expose
    val locationId: Long?,
    @SerializedName("numberOfAdult")
    @Expose
    val numberOfAdult: Int?,
    @SerializedName("numberOfChild")
    @Expose
    val numberOfChild: Int?,
    @SerializedName("numberOfOptionalAdult")
    @Expose
    val numberOfOptionalAdult: Int?,
    @SerializedName("numberOfOptionalChild")
    @Expose
    val numberOfOptionalChild: Int?,
    @SerializedName("serviceId")
    @Expose
    val serviceId: Int?,
    @SerializedName("sessionTimestamp")
    @Expose
    val sessionTimestamp: Long?,
    @SerializedName("userId")
    @Expose
    val userId: Int?,
)

fun CartBookingDTO.toCartBooking() : CartBooking{
    return CartBooking(createdAt, id, lastModified, locationId, numberOfAdult, numberOfChild, numberOfOptionalAdult, numberOfOptionalChild, serviceId, sessionTimestamp, userId)
}