package olmo.wellness.android.domain.model.verification1.response

import olmo.wellness.android.domain.model.verification1.step1.Address
import olmo.wellness.android.domain.model.verification1.step1.ContactPhone

data class Ver1Step1Data(
    val businessLocationId: Int,
    val businessTypeId: Int,
    val businessName: String,
    val address: Address ?=null,
    val contactPhone: ContactPhone?,
    val contactEmail: String?,
)
