package olmo.wellness.android.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CodeRequest(
    @SerializedName("code")
    @Expose
    val code: String ?= null,
)