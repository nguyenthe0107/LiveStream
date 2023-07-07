package olmo.wellness.android.domain.model

import olmo.wellness.android.data.model.user_info.UserInfoEntity

data class UserInfo(
    val userId: Int = 0,
    val token: String = "",
    val refreshToken: String = "",
    val userType: String,
    val expiration: Long = 0,
)

fun UserInfo.toUserInfoEntity() = UserInfoEntity(
    userId = userId,
    token = token,
    refreshToken = refreshToken,
    userType = userType,
    expiration = expiration
)