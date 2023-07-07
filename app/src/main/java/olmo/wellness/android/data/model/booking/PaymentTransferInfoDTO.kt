package olmo.wellness.android.data.model.booking

import olmo.wellness.android.domain.model.booking.PaymentTransferInfo

data class PaymentTransferInfoDTO(
    val paymentReceiptPhoto: String?=null,
    val accountHolderName: String?,
    val bankId: Long?,
    val dateOfTransfer: Long?
)

fun PaymentTransferInfoDTO.toPaymentTransferDomain(): PaymentTransferInfo{
    return PaymentTransferInfo(
        paymentReceiptPhoto = paymentReceiptPhoto,
        accountHolderName = accountHolderName,
        bankId = bankId,
        dateOfTransfer = dateOfTransfer
    )
}
