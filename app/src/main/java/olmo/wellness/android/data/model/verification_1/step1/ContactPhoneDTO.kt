package olmo.wellness.android.data.model.verification_1.step1

import olmo.wellness.android.domain.model.verification1.step1.ContactPhone

data class ContactPhoneDTO(
    val countryId: Int,
    val phoneNumber: String
)

fun ContactPhoneDTO.toContactPhone() = ContactPhone(
    countryId = countryId,
    phoneNumber = phoneNumber,
)
