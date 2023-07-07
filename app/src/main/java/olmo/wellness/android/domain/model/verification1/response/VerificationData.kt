package olmo.wellness.android.domain.model.verification1.response

data class VerificationData(
    val step11: Ver1Step1Data?=null,
    val step12: Ver1Step2Data?=null,
    val step13: Ver1Step3Data?=null,
    val step14: Ver1Step4Data?=null,
    val currentSiv1Step: Int?=null,
    val currentSiv2Step: Int?=null,
    val siv1VerificationStatus: String?=null,
    val siv2VerificationStatus: String?=null,
    val verification: Verification?=null,
    val verificationDetail: List<VerificationDetailData>?=null
)
