package olmo.wellness.android.domain.model.config_app

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ConfigAppModel(
    @SerializedName("ANDROID")
    @Expose
    val android : AndroidConfigModel ?= null
)
