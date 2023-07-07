package olmo.wellness.android.data.model.profile.update

import olmo.wellness.android.data.model.profile.ProfileInfoDTO

data class ProfileInfoUpdateResponse(
    val modified: List<ProfileInfoDTO> = emptyList()
)

