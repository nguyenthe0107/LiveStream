package olmo.wellness.android.domain.model.verification1.step1

import olmo.wellness.android.data.model.verification_1.step1.ContactPhoneDTO

data class ContactPhone(
    val countryId: Int,
    val phoneNumber: String
)

fun ContactPhone.toContactPhoneDTO() = ContactPhoneDTO(
    countryId = countryId,
    phoneNumber = phoneNumber
)