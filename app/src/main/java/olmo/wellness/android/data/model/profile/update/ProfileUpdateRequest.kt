package olmo.wellness.android.data.model.profile.update

data class ProfileUpdateRequest(
    val avatar : String ?= null,
    val birthday: String ?= null,
    val name : String ?= null,
    val bio: String ?= null,
    val gender : String ?= null,
    val email: String?=null,
    val phoneNumber: String ?= null,
    val verificationCode: String ?= null
)