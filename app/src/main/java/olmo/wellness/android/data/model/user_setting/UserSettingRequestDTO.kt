package olmo.wellness.android.data.model.user_setting

import olmo.wellness.android.domain.model.user_setting.UserSettingRequest

data class UserSettingRequestDTO(
    val update : UserSettingDTO ?= null
)

fun UserSettingRequestDTO.toUserSettingRequestDomain() = UserSettingRequest(
    update = update?.toUserSettingDomain()
)


