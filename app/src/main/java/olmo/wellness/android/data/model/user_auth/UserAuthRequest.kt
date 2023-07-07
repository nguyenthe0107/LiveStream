package olmo.wellness.android.data.model.user_auth

data class UserAuthRequest(
    val newIdentity: String,
    val otp: String ?= null
)
