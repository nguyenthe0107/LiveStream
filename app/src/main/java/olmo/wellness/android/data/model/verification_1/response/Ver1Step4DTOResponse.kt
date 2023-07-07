package olmo.wellness.android.data.model.verification_1.response

import olmo.wellness.android.domain.model.verification1.response.Ver1Step4Data

data class Ver1Step4DTOResponse(
    val bankId: Int?,
    val countryId: Int?,
    val holderName: String?,
    val accountNumber: String?,
    val bankBranch: String?,
    val paypalAccessToken: String?
)

fun Ver1Step4DTOResponse.toVer1Step4Data() = Ver1Step4Data(
    bankId = bankId,
    countryId = countryId,
    holderName = holderName,
    accountNumber = accountNumber,
    bankBranch = bankBranch,
    paypalAccessToken = paypalAccessToken
)
