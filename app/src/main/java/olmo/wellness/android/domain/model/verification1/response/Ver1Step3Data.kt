package olmo.wellness.android.domain.model.verification1.response

data class Ver1Step3Data(
    val storeName: String?= null,
    val isHaveServiceLicense: Boolean?= null,
    val subCategoryIds: List<Int>?= null,
    val serviceLicenses: List<String> ?= emptyList(),
)
