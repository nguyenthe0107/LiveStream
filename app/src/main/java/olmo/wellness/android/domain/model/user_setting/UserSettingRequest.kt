package olmo.wellness.android.domain.model.user_setting

import olmo.wellness.android.data.model.user_setting.UserSettingRequestDTO

data class UserSettingRequest(
    val update: UserSetting ?= null
)

fun UserSettingRequest.toUserSettingRequestDTO() = UserSettingRequestDTO(
    update = update?.toUserSettingDTO()
)

