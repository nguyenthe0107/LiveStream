package olmo.wellness.android.domain.model.verification1.response

import olmo.wellness.android.domain.model.verification1.step1.ContactPhone
import olmo.wellness.android.domain.model.verification1.step2.AddressDetail
import olmo.wellness.android.domain.model.verification1.step2.Identity
import olmo.wellness.android.domain.model.verification1.step2.SellerInfo

data class Ver1Step2Data(
    val info: SellerInfo,
    val identity: Identity,
    val address: List<AddressDetail>,
    val phone: List<ContactPhone>,
    val sellerRole: String?
)
