package olmo.wellness.android.data.model.verify_code

data class VerifyCodeRequest(
    val identity: String ?= null,
    val otp: String ?= null,
    val newPassword: String ?=null
)
