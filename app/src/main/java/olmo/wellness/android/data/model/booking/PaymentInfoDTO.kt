package olmo.wellness.android.data.model.booking

import olmo.wellness.android.domain.model.booking.PaymentInfo

data class PaymentInfoDTO(
    val id: Double,
    val paymentStatus: String,
    val paymentMethod: String,
    val voucherUsed: String,
    val voucherRedemptionId: String,
    val voucherRollbackReason: String,
    val redemptionRollbackDate: Long,
    val voucherReductionAmount: Int,
    val paymentTransferInfo: PaymentTransferInfoDTO ?= null
)

fun PaymentInfoDTO.totPaymentInfoDomain() : PaymentInfo {
    return PaymentInfo(
        id, paymentStatus, paymentMethod, voucherUsed, voucherRedemptionId,
        voucherRollbackReason, redemptionRollbackDate, voucherReductionAmount,
        paymentTransferInfo?.toPaymentTransferDomain()
    )
}
