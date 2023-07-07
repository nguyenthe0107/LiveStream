package olmo.wellness.android.domain.model.user_info

import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.domain.model.business.StoreOwner
import olmo.wellness.android.domain.model.profile.ProfileInfo
import olmo.wellness.android.domain.model.verification1.step1.Address

data class UserInfoLocal(
    val userTypeModel: UserTypeModel ?= null,
    val userId: Int ?= null,
    val storeId: Int ?= null,
    val store : StoreOwner?=null,
    val identity: String ?= null,
    val auMethod: String ?= null,
    val address: Address ?= null,
    val verified : Boolean = false,
    val name: String ?= null,
    val serviceName: String ?= null,
    val email: String ?= null,
    val avatar: String ?= null,
    val firebaseToken: String ?= null,
    val methodTracking: String ?= null,
    val profileInfo: ProfileInfo ?= null,
)
