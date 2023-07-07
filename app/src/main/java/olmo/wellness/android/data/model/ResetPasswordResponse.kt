package olmo.wellness.android.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResetPasswordResponse(
    @SerializedName("accessToken")
    @Expose
    val accessToken: AccessTokenModel,

    @SerializedName("userId")
    @Expose
    val userId: Number
)