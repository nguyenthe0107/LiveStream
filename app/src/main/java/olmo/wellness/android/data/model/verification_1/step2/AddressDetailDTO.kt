package olmo.wellness.android.data.model.verification_1.step2

import olmo.wellness.android.domain.model.verification1.step2.AddressDetail

data class AddressDetailDTO(
    val addressType: String,
    val addressInfo: String,
)

fun AddressDetailDTO.toAddressDetailDomain() = AddressDetail(
    addressType = addressType,
    addressInfo = addressInfo,
)
