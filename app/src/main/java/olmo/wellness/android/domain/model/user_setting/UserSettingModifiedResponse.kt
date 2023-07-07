package olmo.wellness.android.domain.model.user_setting

import olmo.wellness.android.data.model.user_setting.UserSettingModifiedResponseDTO

data class UserSettingModifiedResponse(
    val modified : List<UserSetting> = emptyList()
)

fun UserSettingModifiedResponse.toUserSettingModifiedDTO(): UserSettingModifiedResponseDTO {
    return UserSettingModifiedResponseDTO(
        modified = modified.map {
            it.toUserSettingDTO()
        }
    )
}
