package olmo.wellness.android.data.model.notification


import com.google.gson.annotations.SerializedName
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo

data class PayloadNotification(
    @SerializedName("livestream")
    val livestream: LiveSteamShortInfo? = null
)