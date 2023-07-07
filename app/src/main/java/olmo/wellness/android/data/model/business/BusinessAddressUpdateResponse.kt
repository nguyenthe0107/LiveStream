package olmo.wellness.android.data.model.business

import olmo.wellness.android.data.model.verification_1.step1.AddressDTO

data class BusinessAddressUpdateResponse(
    val modified: List<AddressDTO> = emptyList()
)

