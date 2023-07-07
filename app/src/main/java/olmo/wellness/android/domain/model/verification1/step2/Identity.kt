package olmo.wellness.android.domain.model.verification1.step2

import olmo.wellness.android.data.model.verification_1.step2.IdentityDTO

data class Identity(
    val type: String?,
    val identityNumber: String?,
    val countryOfIssue: Int?,
    val dateOfExpiration: String?,
    val documentUris: List<String>?,
)

fun Identity.toIdentityDTO() =
    IdentityDTO(
        type = type, identityNumber = identityNumber,
        countryOfIssue = countryOfIssue,
        dateOfExpiration = dateOfExpiration,
        documentUris = documentUris
    )