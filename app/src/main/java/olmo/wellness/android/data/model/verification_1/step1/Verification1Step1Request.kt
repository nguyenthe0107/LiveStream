package olmo.wellness.android.data.model.verification_1.step1

data class Verification1Step1Request(
    val businessLocationId: Int?,
    val businessTypeId: Int?,
    val businessName: String?,
    val address: AddressDTO?=null,
    val contactPhone: ContactPhoneDTO?,
    val contactEmail: String?
)
