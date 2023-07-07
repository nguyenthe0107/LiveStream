package olmo.wellness.android.domain.model.verification1.response

data class Ver1Step4Data(
    val bankId: Int ?= null,
    val countryId: Int ?= null,
    val holderName: String?= null,
    val accountNumber: String ?= null,
    val bankBranch: String ?= null,
    val paypalAccessToken : String?= null
)
