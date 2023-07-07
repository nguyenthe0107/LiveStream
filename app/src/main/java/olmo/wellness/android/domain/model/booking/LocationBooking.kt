package olmo.wellness.android.domain.model.booking

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LocationBooking(
    @SerializedName("adultPrice")
    @Expose
    val adultPrice: Int?,
    @SerializedName("adultPriceNote")
    @Expose
    val adultPriceNote: String?,
    @SerializedName("artDate")
    @Expose
    val artDate: Long?,
    @SerializedName("childPrice")
    @Expose
    val childPrice: Int?,
    @SerializedName("childPriceNote")
    @Expose
    val childPriceNote: String?,
    @SerializedName("day")
    @Expose
    val day: String?,
    @SerializedName("endDate")
    @Expose
    val endDate: Long?,
    @SerializedName("optionalAdultPrice")
    @Expose
    val optionalAdultPrice: Int?,
    @SerializedName("optionalAdultPriceNote")
    @Expose
    val optionalAdultPriceNote: Any?,
    @SerializedName("optionalChildPrice")
    @Expose
    val optionalChildPrice: Int?,
    @SerializedName("optionalChildPriceNote")
    @Expose
    val optionalChildPriceNote: Any?
)