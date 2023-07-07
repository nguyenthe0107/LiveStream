package olmo.wellness.android.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.data.model.definition.AuthMethod

class LoginRequest(
    @Expose
    @SerializedName("authMethod")
    val authMethod : AuthMethod ?= null,

    @Expose
    @SerializedName("authData")
    val authData : ArrayList<String> = arrayListOf()
)