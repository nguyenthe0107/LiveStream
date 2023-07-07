package olmo.wellness.android.data.model.user_info

import androidx.room.Entity
import androidx.room.PrimaryKey
import olmo.wellness.android.domain.model.UserInfo

@Entity(tableName = "user_info_table")
data class UserInfoEntity(
    @PrimaryKey(autoGenerate = false)
    val userId: Int = 0,
    val token: String = "",
    val refreshToken: String = "",
    val userType: String,
    val expiration: Long = 0,
)

fun UserInfoEntity.toUserInfoDomain(): UserInfo {
    return UserInfo(
        userId = userId,
        token = token,
        refreshToken = refreshToken,
        userType = userType,
        expiration = expiration
    )
}
