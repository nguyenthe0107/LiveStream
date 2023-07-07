package olmo.wellness.android.data.model.verification_1.step2

import olmo.wellness.android.data.model.verification_1.step1.ContactPhoneDTO

data class Verification1Step2Request(
    val info: SellerInfoDTO?,
    val identity: IdentityDTO?,
    val address: List<AddressDetailDTO>?,
    val phone: List<ContactPhoneDTO>?,
    //val sellerRole: String?
)
