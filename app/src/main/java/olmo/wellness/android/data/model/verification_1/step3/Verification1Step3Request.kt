package olmo.wellness.android.data.model.verification_1.step3

data class Verification1Step3Request(
    val storeName: String?,
    //val isOwner: String,
    //val tradeMarkOwner: String,
    val isHaveServiceLicense: Boolean?,
    val subCategoryIds: List<Int>?,
    val serviceLicenses : List<String>?
)