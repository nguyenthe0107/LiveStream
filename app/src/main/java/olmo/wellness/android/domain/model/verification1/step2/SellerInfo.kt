package olmo.wellness.android.domain.model.verification1.step2

import olmo.wellness.android.data.model.verification_1.step2.SellerInfoDTO

data class SellerInfo(
    val countryOfCitizen: Int?,
    val dateOfBirth: String?,
)

fun SellerInfo.toSellerInfoDTO() = SellerInfoDTO(
    countryOfCitizen = countryOfCitizen,
    dateOfBirth = dateOfBirth
)
