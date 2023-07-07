package olmo.wellness.android.data.model.verification_1.response

import olmo.wellness.android.domain.model.verification1.response.Verification
import olmo.wellness.android.domain.model.verification1.response.VerificationData
import java.io.ObjectInputValidation

data class VerificationDTOResponse(
    val step11: Ver1Step1DTOResponse?,
    val step12: Ver1Step2DTOResponse?,
    val step13: Ver1Step3DTOResponse?,
    val step14: Ver1Step4DTOResponse?,
    val currentSiv1Step: Int,
    val currentSiv2Step: Int,
    val siv1VerificationStatus: String?,
    val siv2VerificationStatus: String?,
    val verification: Verification?,
    val verificationDetail: List<VerificationDetailDTOResponse>?
)

fun VerificationDTOResponse.toVerificationData() = VerificationData(
    step11 = step11?.toVer1Step1Data(),
    step12 = step12?.toVer1Step2Data(),
    step13 = step13?.toVer1Step3Data(),
    step14 = step14?.toVer1Step4Data(),
    currentSiv1Step = currentSiv1Step,
    currentSiv2Step = currentSiv2Step,
    siv1VerificationStatus = siv1VerificationStatus,
    siv2VerificationStatus = siv2VerificationStatus,
    verification = verification,
    verificationDetail = verificationDetail?.map { it.toVerificationDetailData() }
)
