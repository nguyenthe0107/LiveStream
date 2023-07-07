package olmo.wellness.android.data.model.id_api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.data.model.definition.AuthMethod

class RegisterIDApiRequest(
    @Expose
    @SerializedName("authMethod")
    val authMethod : AuthMethod ?= null,

    @Expose
    @SerializedName("authData")
    val authData : ArrayList<String> = arrayListOf()
)