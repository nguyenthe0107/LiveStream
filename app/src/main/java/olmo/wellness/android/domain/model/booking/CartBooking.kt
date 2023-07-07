package olmo.wellness.android.domain.model.booking

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CartBooking(
    @SerializedName("createdAt")
    @Expose
    var createdAt: Long? = null,

    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("lastModified")
    @Expose
    var lastModified: Long? = null,

    @SerializedName("locationId")
    @Expose
    var locationId: Long? = null,

    @SerializedName("numberOfAdult")
    @Expose
    var numberOfAdult: Int? = null,

    @SerializedName("numberOfChild")
    @Expose
    var numberOfChild: Int? = null,

    @SerializedName("numberOfOptionalAdult")
    @Expose
    var numberOfOptionalAdult: Int? = null,

    @SerializedName("numberOfOptionalChild")
    @Expose
    var numberOfOptionalChild: Int? = null,

    @SerializedName("serviceId")
    @Expose
    var serviceId: Int? = null,

    @SerializedName("sessionTimestamp")
    @Expose
    var sessionTimestamp: Long? = null,

    @SerializedName("userId")
    @Expose
    var userId: Int? = null,

    @SerializedName("serviceBooking")
    @Expose
    var serviceBooking: ServiceBookingForCart?=null, // map api detail detail service

    var isChecked : Boolean ?= true
)