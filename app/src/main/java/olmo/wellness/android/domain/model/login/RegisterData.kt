package olmo.wellness.android.domain.model.login

data class RegisterData(
    val userId: Int? = null,
    val accessToken: TokenData? = null,
    val userType: String? = null,
    val verified: Boolean ?= null
)
