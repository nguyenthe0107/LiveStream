package olmo.wellness.android.domain.model.login

data class TokenData(
    val token: String,
    val refreshToken: String,
    val expiration: Long
)
