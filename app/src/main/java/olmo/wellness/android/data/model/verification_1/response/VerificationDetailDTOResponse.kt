package olmo.wellness.android.data.model.verification_1.response

import olmo.wellness.android.domain.model.verification1.response.VerificationDetailData

data class VerificationDetailDTOResponse(
    val id: Int?,
    val verificationId: Int?,
    val fieldName: String?,
    val fieldIssue: String?,
)

fun VerificationDetailDTOResponse.toVerificationDetailData() = VerificationDetailData(
    id = id,
    verificationId = verificationId,
    fieldName = fieldName,
    fieldIssue = fieldIssue,
)