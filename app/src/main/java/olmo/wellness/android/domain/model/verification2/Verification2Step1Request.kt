package olmo.wellness.android.domain.model.verification2
import olmo.wellness.android.data.model.verification_2.Verification2Step1Request
data class V2Step1Request(
    var businessLicense: String = "",
    var brandLicense: String = "",
    var electricBill: String = "",
    var sellReport: String = "",
)

fun V2Step1Request.toStep1RequestDTO() = Verification2Step1Request(
    businessLicense = businessLicense,
    brandLicense = brandLicense,
    electricBill = electricBill,
    sellReport = sellReport
)