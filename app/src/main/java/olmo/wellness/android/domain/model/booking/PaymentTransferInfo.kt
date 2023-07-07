package olmo.wellness.android.domain.model.booking

data class PaymentTransferInfo(
    val paymentReceiptPhoto: String?=null,
    val accountHolderName: String?,
    val bankId: Long?,
    val dateOfTransfer: Long?
)
