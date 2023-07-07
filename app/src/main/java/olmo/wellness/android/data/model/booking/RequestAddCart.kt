package olmo.wellness.android.data.model.booking

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RequestAddCart(
    @SerializedName("locationId")
    @Expose
    var locationId: Long?=null,
    @SerializedName("numberOfAdult")
    @Expose
    var numberOfAdult: Int?=null,
    @SerializedName("numberOfChild")
    @Expose
    var numberOfChild: Int?=null,
    @SerializedName("numberOfOptionalAdult")
    @Expose
    var numberOfOptionalAdult: Int?=null,
    @SerializedName("numberOfOptionalChild")
    @Expose
    var numberOfOptionalChild: Int?=null,
    @SerializedName("serviceId")
    @Expose
    var serviceId: Int?=null,
    @SerializedName("sessionTimestamp")
    @Expose
    var sessionTimestamp: Long?=null,
)