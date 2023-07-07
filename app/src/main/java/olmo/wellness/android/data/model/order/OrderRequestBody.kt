package olmo.wellness.android.data.model.order

import olmo.wellness.android.domain.model.voucher.VoucherRequest

data class OrderRequestBody(
    val paymentMethod: String?,
    val modelableType: String?,
    val modelableId: Int,
    val billingFirstName: String ?=null,
    val billingLastName: String ?=null,
    val billingPhoneNumber: String ?=null,
    val billingEmail: String ?=null,
    val billingAddress: String ?=null,
    val typeDevice: String ="MOBILE",
    val voucherInfo: VoucherRequest?=null,
    val livestreamId: Int ?= null,
    val sessionSecret: String ?= null
)
