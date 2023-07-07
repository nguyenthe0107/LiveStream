package olmo.wellness.android.data.model.user_follow

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserFollowModifiedDTO(
    @SerializedName("modified")
    @Expose
    val list : List<UserFollowDTO> ?= null
)

