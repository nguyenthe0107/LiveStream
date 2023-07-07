package olmo.wellness.android.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AuthDataReq(
    @Expose
    @SerializedName("authData")
    val authData: ArrayList<String> = arrayListOf()
)