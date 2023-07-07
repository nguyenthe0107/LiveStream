package olmo.wellness.android.data.model.verification_1.step2

import olmo.wellness.android.domain.model.verification1.step2.SellerInfo

data class SellerInfoDTO(
    val countryOfCitizen: Int?,
    val dateOfBirth: String?,
)

fun SellerInfoDTO.toSellerInfoDomain() = SellerInfo(
    countryOfCitizen = countryOfCitizen,
    dateOfBirth = dateOfBirth,
)
