package olmo.wellness.android.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class IdentityRequest (
    @SerializedName("identity")
    @Expose
    val identity: String
)