package olmo.wellness.android.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.domain.model.login.LoginData

class LoginResponse(
    @Expose
    @SerializedName("userId")
    val userId: Int? = null,

    @Expose
    @SerializedName("userType")
    val userType: String? = null,

    @Expose
    @SerializedName("accessToken")
    val accessToken: AccessTokenModel? = null,

    @Expose
    @SerializedName("verified")
    val verified: Boolean? = false,
)

fun LoginResponse.toLoginData() =
    LoginData(userId = userId, accessToken = accessToken?.toTokenData(), userType = userType, verified= verified)