package olmo.wellness.android.data.model.user_info

import olmo.wellness.android.domain.model.user_info.UserInfoDomain

data class UserInfoDTO(
    val id : Int,
    val principalId : Int,
    val username : String?,
    val email : String?,
    val phoneNumber : String?,
    val avatar : String?,
    val coverPhoto : String?,
    val bio : String?,
    val tfaSetup : Boolean?,
    val tfaEnabled : Boolean?
)

fun UserInfoDTO.toUserInfoDomain() : UserInfoDomain {
    return UserInfoDomain(id, principalId, username,email, phoneNumber, avatar, coverPhoto, bio, tfaEnabled, tfaEnabled)
}