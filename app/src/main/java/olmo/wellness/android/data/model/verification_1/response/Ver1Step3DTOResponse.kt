package olmo.wellness.android.data.model.verification_1.response
import olmo.wellness.android.domain.model.verification1.response.Ver1Step3Data

data class Ver1Step3DTOResponse(
    val storeName: String?= null,
    val isHaveServiceLicense: Boolean?= null,
    val subCategoryIds: List<Int>?= null,
    val serviceLicenses: List<String>,
)

fun Ver1Step3DTOResponse.toVer1Step3Data() = Ver1Step3Data(
    storeName = storeName,
    isHaveServiceLicense = isHaveServiceLicense,
    subCategoryIds = subCategoryIds,
    serviceLicenses = serviceLicenses
)