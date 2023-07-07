package olmo.wellness.android.data.model.user_setting

import olmo.wellness.android.domain.model.user_setting.UserSettingModifiedResponse

data class UserSettingModifiedResponseDTO(
    val modified : List<UserSettingDTO> = emptyList()
)

fun UserSettingModifiedResponseDTO.toUserSettingDomain(): UserSettingModifiedResponse {
    return UserSettingModifiedResponse(
        modified = modified.map {
            it.toUserSettingDomain()
        }
    )
}

