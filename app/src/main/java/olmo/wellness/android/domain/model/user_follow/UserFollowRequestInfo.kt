package olmo.wellness.android.domain.model.user_follow

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.data.model.user_follow.UserFollowDTO

data class UserFollowRequestInfo(
    @SerializedName("id")
    @Expose
    val id : List<Int> ?= null,

    @SerializedName("createdAt")
    @Expose
    val createdAt : Long ?= null,

    @SerializedName("lastModified")
    @Expose
    val lastModified : Long ?= null,

    @SerializedName("userId")
    @Expose
    val userId : List<Int> ?= null,

    @SerializedName("businessId")
    @Expose
    val businessId : Int ?= null,

    val livestreamId : List<Int> ?= null,

    val followedUserId: List<Int> ?= null
)
