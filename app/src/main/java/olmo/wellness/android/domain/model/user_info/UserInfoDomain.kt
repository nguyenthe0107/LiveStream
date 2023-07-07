package olmo.wellness.android.domain.model.user_info

data class UserInfoDomain(
    val id : Int,
    val principalId : Int?,
    val username : String?,
    val email : String?,
    val phoneNumber : String?,
    val avatar : String?,
    val coverPhoto : String?,
    val bio : String?,
    val tfaSetup : Boolean?,
    val tfaEnabled : Boolean?
)


