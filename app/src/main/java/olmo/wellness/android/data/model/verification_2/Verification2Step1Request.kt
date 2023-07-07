package olmo.wellness.android.data.model.verification_2

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Verification2Step1Request(
    @Expose
    @SerializedName("businessLicense")
    val businessLicense: String,

    @Expose
    @SerializedName("brandLicense")
    val brandLicense: String,

    @Expose
    @SerializedName("electricBill")
    val electricBill: String,

    @Expose
    @SerializedName("sellReport")
    val sellReport: String
)
