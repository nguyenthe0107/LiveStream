package olmo.wellness.android.data.model.profile.update

import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.domain.model.profile.ProfileInfo

data class ProfileInfoUpdateDTO(
    val id: Int? = null,
    val createdAt: Long? = null,
    val lastModified: Long? = null,
    val principalId: Int? = null,
    val avatar: String? = null,
    val birthday: String? = null,
    val name: String? = null,
    val storeName: String? = null,
    val serviceName: String? = null,
    val username: String? = null,
    val bio: String? = null,
    val gender: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val resetingPassword: Boolean? = false,
    val verified: Boolean? = false,
    val verificationCode: String? = null,
    val failedVerificationAttempts: Int? = null,
    val requestedVerificationCount: Int? = null,
    val mfaSecret: String? = null,
    val useTfa: Boolean? = false,
    val userTypeModel: UserTypeModel? = null,
    val displayName: String? = null,
)

fun ProfileInfoUpdateDTO.toProfileInfoDomain(): ProfileInfo {
    return ProfileInfo(
        id,
        createdAt,
        lastModified,
        principalId,
        avatar,
        birthday,
        name,
        storeName,
        serviceName,
        username,
        bio,
        gender,
        email,
        phoneNumber,
        resetingPassword,
        verified,
        verificationCode,
        failedVerificationAttempts,
        requestedVerificationCount,
        mfaSecret,
        useTfa,
        userTypeModel,
        displayName
    )
}
