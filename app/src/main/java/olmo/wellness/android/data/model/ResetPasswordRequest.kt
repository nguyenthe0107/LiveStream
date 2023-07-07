package olmo.wellness.android.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResetPasswordRequest(
    @SerializedName("identity")
    @Expose
    val identity : String,

    @SerializedName("code")
    @Expose
    val code : String,

    @SerializedName("newPassword")
    @Expose
    val newPassword: String
)