package olmo.wellness.android.data.model.profile

import olmo.wellness.android.data.model.business.StoreDTO
import olmo.wellness.android.data.model.business.toStoreOwnerDomain
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.domain.model.profile.ProfileInfo

data class ProfileInfoDTO(
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
    val resetingPassword: Boolean? = null,
    val verified: Boolean? = null,
    val verificationCode: String? = null,
    val failedVerificationAttempts: Int? = null,
    val requestedVerificationCount: Int? = null,
    val mfaSecret: String? = null,
    val useTfa: Boolean? = null,
    val userType: UserTypeModel? = null,
    val displayName: String? = null,
    val storeId: Int ?= null,
    val store: StoreDTO ?= null
)

fun ProfileInfoDTO.toProfileInfoDomain(): ProfileInfo {
    return ProfileInfo(
        id = id,
        createdAt = createdAt,
        lastModified = lastModified,
        principalId = principalId,
        avatar = avatar,
        birthday = birthday,
        name = name,
        storeName= storeName,
        serviceName = serviceName,
        username = username,
        bio = bio,
        gender = gender,
        email = email,
        phoneNumber = phoneNumber,
        resetingPassword = resetingPassword,
        verified = verified,
        verificationCode = verificationCode,
        failedVerificationAttempts = failedVerificationAttempts,
        requestedVerificationCount = requestedVerificationCount,
        mfaSecret=mfaSecret,
        useTfa = useTfa,
        userType = userType,
        displayName = displayName,
        storeId = storeId,
        store = store?.toStoreOwnerDomain()
    )
}