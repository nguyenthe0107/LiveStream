@file:JvmName("Step3RequestKt")

package olmo.wellness.android.domain.model.verification1.step3
import olmo.wellness.android.data.model.verification_1.step3.Verification1Step3Request

data class Step3Request(
    val storeName: String?,
    val isHaveServiceLicense: Boolean?,
    val subCategoryIds: List<Int>?,
    val serviceLicenses: List<String>?,
)

fun Step3Request.toStep3RequestDTO() = Verification1Step3Request(
    storeName = storeName,
    isHaveServiceLicense = isHaveServiceLicense,
    subCategoryIds = subCategoryIds,
    serviceLicenses = serviceLicenses,
)