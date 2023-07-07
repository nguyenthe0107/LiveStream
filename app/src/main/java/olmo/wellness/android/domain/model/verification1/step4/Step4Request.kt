package olmo.wellness.android.domain.model.verification1.step4

import olmo.wellness.android.data.model.verification_1.step4.Verification1Step4Request

data class Step4Request(
    val bankId: Int?,
    val countryId: Int?,
    val holderName: String?,
    val accountNumber: String?,
    val bankBranch: String?,
    val paypalAccessToken : String?
)

fun Step4Request.toStep4RequestDTO() = Verification1Step4Request(
    bankId = bankId,
    countryId = countryId,
    holderName = holderName,
    accountNumber = accountNumber,
    bankBranch = bankBranch,
    paypalAccessToken = paypalAccessToken
)