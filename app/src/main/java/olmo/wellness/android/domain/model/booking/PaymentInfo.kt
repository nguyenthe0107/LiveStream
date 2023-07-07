package olmo.wellness.android.domain.model.booking


data class PaymentInfo(
    val id: Double?,
    val paymentStatus: String?,
    val paymentMethod: String?,
    val voucherUsed: String?,
    val voucherRedemptionId: String?,
    val voucherRollbackReason: String?,
    val redemptionRollbackDate: Long?,
    val voucherReductionAmount: Int?,
    val paymentTransferInfo: PaymentTransferInfo?= null
)
