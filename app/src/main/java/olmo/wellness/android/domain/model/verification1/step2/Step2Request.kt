package olmo.wellness.android.domain.model.verification1.step2

import olmo.wellness.android.data.model.verification_1.step2.Verification1Step2Request
import olmo.wellness.android.domain.model.verification1.step1.ContactPhone
import olmo.wellness.android.domain.model.verification1.step1.toContactPhoneDTO

data class Step2Request(
    val info: SellerInfo?,
    val identity: Identity?,
    val address: List<AddressDetail>?,
    val phone: List<ContactPhone>?,
    //val sellerRole: String?
)

fun Step2Request.toStep2RequestDTO() = Verification1Step2Request(
    info = info?.toSellerInfoDTO(),
    identity = identity?.toIdentityDTO(), address = address?.map { it.toAddressDetailDTO() },
    phone = phone?.map { it.toContactPhoneDTO() },
    //sellerRole = sellerRole
)