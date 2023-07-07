package olmo.wellness.android.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.domain.model.login.TokenData

class AccessTokenModel(
    @SerializedName("token")
    @Expose
    val token: String,

    @SerializedName("refreshToken")
    @Expose
    val refreshToken: String,

    @SerializedName("expiration")
    @Expose
    val expiration: Long
)

fun AccessTokenModel.toTokenData() =
    TokenData(token = token, refreshToken = refreshToken, expiration = expiration)