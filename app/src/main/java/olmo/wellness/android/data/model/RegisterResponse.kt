package olmo.wellness.android.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.domain.model.login.RegisterData

class RegisterResponse(
    @Expose
    @SerializedName("userId")
    val userId: Int? = null,

    @Expose
    @SerializedName("accessToken")
    val accessToken: AccessTokenModel? = null,

    @Expose
    @SerializedName("verified")
    val verified: Boolean? = null,

    @Expose
    @SerializedName("userType")
    val userType: String? = null
)

fun RegisterResponse.toRegisterData() = RegisterData(
    userId = userId,
    accessToken = accessToken?.toTokenData(),
    verified = verified,
    userType = userType
)