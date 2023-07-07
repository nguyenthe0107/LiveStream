package olmo.wellness.android.domain.model.login

import olmo.wellness.android.domain.model.business.StoreOwner

data class UserInfoResponse(
    val userType: String? = "",
    val id: Int?=null,
    val principalId: Int?=null,
    val name: String?=null,
    val store : StoreOwner?=null,
    val verified: Boolean?=null,
    val email: String?=null,
    val phoneNumber: String?=null,
    val avatar: String?=null,
    val coverPhoto: String?=null,
    val bio: String?=null,
    val mfaSecret: Boolean?=null,
    val useTfa: Boolean?=null,
    val birthday: String ?= null,
    val gender: Boolean ?= null
)
