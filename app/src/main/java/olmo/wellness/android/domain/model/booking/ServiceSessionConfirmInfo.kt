package olmo.wellness.android.domain.model.booking

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ServiceSessionConfirmInfo(
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
    val serviceSessionInfo: List<ServiceSessionInfo>?
)
