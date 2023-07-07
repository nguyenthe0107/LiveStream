package olmo.wellness.android.data.model.booking

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.domain.model.booking.ServiceSessionConfirmInfo

data class ServiceSessionConfirmDTO(
    @SerializedName("id")
    @Expose
    val id: Double?=null,

    @SerializedName("totalPrice")
    @Expose
    val totalPrice: Float?=null,

    @SerializedName("vat")
    @Expose
    val vat: Float?=null,

    @SerializedName("priceServices")
    @Expose
    val serviceSessionInfoDTO: List<ServiceSessionInfoDTO>?
)

fun ServiceSessionConfirmDTO.toServiceSessionConfirmDomain(): ServiceSessionConfirmInfo{
    return ServiceSessionConfirmInfo(
        id = id,
        totalPrice = totalPrice,
        vat = vat,
        serviceSessionInfo = serviceSessionInfoDTO?.map {
            it.toServiceSessionDomain()
        }
    )
}
