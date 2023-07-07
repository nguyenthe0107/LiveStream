package olmo.wellness.android.data.model.booking


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.domain.model.booking.LocationBooking

data class LocationDTO(
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

fun LocationDTO.toLocationBooking() : LocationBooking{
    return LocationBooking(adultPrice, adultPriceNote, artDate, childPrice, childPriceNote, day, endDate, optionalAdultPrice, optionalAdultPriceNote, optionalChildPrice, optionalChildPriceNote)
}