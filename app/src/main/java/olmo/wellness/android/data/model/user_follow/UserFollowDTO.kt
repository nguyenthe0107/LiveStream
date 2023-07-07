package olmo.wellness.android.data.model.user_follow

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.data.model.business.StoreDTO
import olmo.wellness.android.domain.model.business.toStoreOwnerDomain
import olmo.wellness.android.domain.model.user_follow.UserFollowInfo

data class UserFollowDTO(
    @SerializedName("id")
    @Expose
    val id : Int ?= null,

    @SerializedName("name")
    @Expose
    val name : String ?= null,

    @SerializedName("firstName")
    @Expose
    val firstName : String ?= null,

    @SerializedName("lastName")
    @Expose
    val lastName : String ?= null,

    @SerializedName("avatar")
    @Expose
    val avatar : String ?= null,

    @SerializedName("store")
    @Expose
    val storeModel : StoreDTO ?= null,

    @SerializedName("createdAt")
    @Expose
    val createdAt : Long ?= null,

    @SerializedName("lastModified")
    @Expose
    val lastModified : Long ?= null,

    @SerializedName("userId")
    @Expose
    val userId : Int ?= null,

    @SerializedName("businessId")
    @Expose
    val businessId : Int ?= null,

    @SerializedName("livestreamId")
    @Expose
    val livestreamId : Int ?= null
)

fun UserFollowDTO.toUserFollowDomain() : UserFollowInfo {
    return UserFollowInfo(
        id = id,
        name = name,
        firstName = firstName,
        lastName = lastName,
        avatar = avatar,
        storeModel = storeModel?.toStoreOwnerDomain(),
        createdAt = createdAt,
        lastModified = lastModified,
        userId = userId,
        businessId = businessId,
        livestreamId = livestreamId
    )
}
