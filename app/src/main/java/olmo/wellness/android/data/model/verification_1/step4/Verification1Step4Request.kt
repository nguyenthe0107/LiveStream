package olmo.wellness.android.data.model.verification_1.step4

data class Verification1Step4Request(
    val bankId: Int?,
    val countryId: Int?,
    val holderName: String?,
    val accountNumber: String?,
    val bankBranch: String?,
    val paypalAccessToken: String?
)
