package olmo.wellness.android.ui.screen.profile_dashboard.edit_bio

import olmo.wellness.android.domain.model.profile.ProfileInfo

data class EditBioState(
    val isValid: Boolean? = null,
    val errMessage: String = "",
    val message: String? = null,
    val showLoading: Boolean = false,
    val profileInfo: ProfileInfo? = null,
    val isUpdateBioSuccess: Boolean = false
)

sealed class EditBioEvent{
    data class Validate(
        val isValid: Boolean? = null,
        val errMessage: String = "",
        val message: String? = null
    ): EditBioEvent()

    data class ShowLoading(
        val isLoading: Boolean = false
    ): EditBioEvent()

    data class LoadDefaultValueSuccess(
        val profileInfo: ProfileInfo,
    ): EditBioEvent()

    data class UpdateBioSuccess(
        val isUpdateBioSuccess: Boolean,
    ): EditBioEvent()
}
