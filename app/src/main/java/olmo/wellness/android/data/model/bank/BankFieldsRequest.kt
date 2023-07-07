package olmo.wellness.android.data.model.bank

data class BankFieldsRequest(
    val id: Int? = null,
    val countryId: List<Int> = listOf(),
    val bankName: String? = null
)
