package olmo.wellness.android.data.model.booking


import com.google.gson.annotations.SerializedName
import olmo.wellness.android.data.model.business.StoreDTO
import olmo.wellness.android.data.model.business.toStoreOwnerDomain
import olmo.wellness.android.domain.model.booking.DetailBookingPublic

data class DetailBookingPublicDTO(
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
    val storeInfo: StoreDTO?,
    @SerializedName("verified")
    val verified: Boolean?
)

fun DetailBookingPublicDTO.toDetailBookingService() : DetailBookingPublic{
    return DetailBookingPublic(categoryId,currency,description,duration,durationType,id,name,photos,sales,startingPrice,storeInfo?.toStoreOwnerDomain(),verified)
}