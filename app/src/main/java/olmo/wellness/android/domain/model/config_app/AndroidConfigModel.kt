package olmo.wellness.android.domain.model.config_app

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AndroidConfigModel(
    @SerializedName("DEV")
    @Expose
    val dev: Int,

    @SerializedName("STG")
    @Expose
    val staging: Int,

    @SerializedName("PRO")
    @Expose
    val production: Int,
)