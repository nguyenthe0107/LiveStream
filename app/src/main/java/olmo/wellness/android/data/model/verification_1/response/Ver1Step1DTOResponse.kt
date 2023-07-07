package olmo.wellness.android.data.model.verification_1.response

import olmo.wellness.android.data.model.verification_1.step1.AddressDTO
import olmo.wellness.android.data.model.verification_1.step1.ContactPhoneDTO
import olmo.wellness.android.data.model.verification_1.step1.toAddressDomain
import olmo.wellness.android.data.model.verification_1.step1.toContactPhone
import olmo.wellness.android.domain.model.verification1.response.Ver1Step1Data

data class Ver1Step1DTOResponse(
    val businessLocationId: Int,
    val businessTypeId: Int,
    val businessName: String,
    val address: AddressDTO?,
    val contactPhone: ContactPhoneDTO?,
    val contactEmail: String?,
)

fun Ver1Step1DTOResponse.toVer1Step1Data() = Ver1Step1Data(
    businessLocationId = businessLocationId,
    businessTypeId = businessTypeId,
    businessName = businessName,
    address = address?.toAddressDomain(),
    contactPhone = contactPhone?.toContactPhone(),
    contactEmail = contactEmail,
)