package olmo.wellness.android.data.model.voucher

import olmo.wellness.android.domain.model.voucher.VoucherValidateInfo

data class VoucherValidateModelDTO(
    val valid: Boolean,
    val errorMessage: String?,
    val reductionAmount: Double?
)

fun VoucherValidateModelDTO.toVoucherDomain(): VoucherValidateInfo {
    return VoucherValidateInfo(
        valid = valid,
        errorMessage = errorMessage,
        reductionAmount = reductionAmount
    )
}

