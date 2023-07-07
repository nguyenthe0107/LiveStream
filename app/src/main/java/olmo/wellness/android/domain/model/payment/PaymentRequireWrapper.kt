package olmo.wellness.android.domain.model.payment

import olmo.wellness.android.domain.model.voucher.VoucherRequest

data class PaymentRequireWrapper(
    val billingFirstName: String?=null,
    val billingLastName: String?=null,
    val billingPhoneNumber: String?=null,
    val billingEmail: String?=null,
    val voucherRequest: VoucherRequest?=null,
    val bookingId: Double?=null,
    val totalMoney: Float?=null,
    val timeStamp: Long ?= null,
    val sessionSecret: String ?= null
)
