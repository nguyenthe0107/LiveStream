package olmo.wellness.android.domain.model.login

data class LoginData(
    val userId: Int? = null,
    val accessToken: TokenData? = null,
    val userType: String? = null,
    val verified: Boolean ?= false,
    val userName: String ?= null
)
