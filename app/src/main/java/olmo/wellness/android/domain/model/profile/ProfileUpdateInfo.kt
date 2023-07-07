package olmo.wellness.android.domain.model.profile

import olmo.wellness.android.data.model.definition.UserTypeModel

data class ProfileUpdateInfo (
    val id: List<Int?>? = null,
    val createdAt: Int? = null,
    val lastModified: Int? = null,
    val principalId: Int? = null,
    val avatar: String? = null,
    val birthday: String? = null,
    val name: String? = null,
    val bio: String? = null,
    val gender: List<String>? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val resetingPassword: Boolean? = null,
    val verified: Boolean? = null,
    val verificationCode: String? = null,
    val failedVerificationAttempts: Int? = null,
    val requestedVerificationCount: Int? = null,
    val mfaSecret: String? = null,
    val useTfa: Boolean? = null,
    val userTypeModel: UserTypeModel? = null,
    val displayName: String? = null,
)