package olmo.wellness.android.data.model.booking

import olmo.wellness.android.domain.model.booking.BillingInfo

data class BillingInfoDTO(
    val billingFirstName: String?,
    val billingLastName: String?,
    val billingPhoneNumber: String?,
    val billingEmail: String?,
    val billingAddress: String?
)

fun BillingInfoDTO.toConvertBillingInfoDomain() : BillingInfo{
    return BillingInfo(
        billingFirstName = billingFirstName,
        billingLastName = billingLastName,
        billingPhoneNumber = billingPhoneNumber,
        billingEmail = billingEmail,
        billingAddress = billingAddress
    )
}
