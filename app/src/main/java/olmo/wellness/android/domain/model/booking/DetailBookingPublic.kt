package olmo.wellness.android.domain.model.booking


import com.google.gson.annotations.SerializedName
import olmo.wellness.android.domain.model.business.StoreOwner

data class DetailBookingPublic(
    @SerializedName("categoryId")
    val categoryId: Int?,
    @SerializedName("currency")
    val currency: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("duration")
    val duration: Int?,
    @SerializedName("durationType")
    val durationType: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("photos")
    val photos: List<String?>?,
    @SerializedName("sales")
    val sales: Int?,
    @SerializedName("startingPrice")
    val startingPrice: Long?,
    @SerializedName("storeInfo")
    val storeInfo: StoreOwner?,
    @SerializedName("verified")
    val verified: Boolean?
)