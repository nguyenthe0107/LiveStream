package olmo.wellness.android.domain.model.voucher

data class VoucherValidateInfo(
    val valid: Boolean,
    val errorMessage: String?,
    val reductionAmount: Double?
)
