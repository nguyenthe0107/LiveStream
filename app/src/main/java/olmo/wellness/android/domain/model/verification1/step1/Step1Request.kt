package olmo.wellness.android.domain.model.verification1.step1

import olmo.wellness.android.data.model.verification_1.step1.Verification1Step1Request

data class Step1Request(
    val businessLocationId: Int?,
    val businessTypeId: Int?,
    val businessName: String?,
    val address: Address?,
    val contactPhone: ContactPhone? = null,
    val contactEmail: String? = null
)

fun Step1Request.toStep1RequestDTO() = Verification1Step1Request(
    businessLocationId = businessLocationId,
    businessTypeId = businessTypeId,
    businessName = businessName,
    address = address?.toAddressDTO(),
    contactPhone = contactPhone?.toContactPhoneDTO(),
    contactEmail = contactEmail
)
