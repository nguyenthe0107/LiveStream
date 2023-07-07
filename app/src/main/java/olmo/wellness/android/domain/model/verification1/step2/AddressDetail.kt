package olmo.wellness.android.domain.model.verification1.step2

import olmo.wellness.android.data.model.verification_1.step2.AddressDetailDTO

data class AddressDetail(
    val addressType: String,
    val addressInfo: String,
)

fun AddressDetail.toAddressDetailDTO() =
    AddressDetailDTO(addressInfo = addressInfo, addressType = addressType)
