package olmo.wellness.android.domain.model.profile

import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.domain.model.business.StoreOwner

data class ProfileInfo(
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
    val storeId : Int ?= null,
    val store: StoreOwner ?= null
)
