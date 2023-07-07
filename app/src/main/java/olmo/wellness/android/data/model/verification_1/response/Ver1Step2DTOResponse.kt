package olmo.wellness.android.data.model.verification_1.response

import olmo.wellness.android.data.model.verification_1.step1.ContactPhoneDTO
import olmo.wellness.android.data.model.verification_1.step1.toContactPhone
import olmo.wellness.android.data.model.verification_1.step2.*
import olmo.wellness.android.domain.model.verification1.response.Ver1Step2Data

data class Ver1Step2DTOResponse(
    val info: SellerInfoDTO,
    val identity: IdentityDTO,
    val address: List<AddressDetailDTO>,
    val phone: List<ContactPhoneDTO>,
    val sellerRole: String?
)

fun Ver1Step2DTOResponse.toVer1Step2Data() = Ver1Step2Data(
    info = info.toSellerInfoDomain(),
    identity = identity.toIdentityDomain(),
    address = address.map { it.toAddressDetailDomain() },
    phone = phone.map { it.toContactPhone() },
    sellerRole = sellerRole,
)