package olmo.wellness.android.domain.model.fcm

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.data.model.notification.PayloadNotification
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo

data class NotificationModel(
    @SerializedName("payload")
    @Expose
    val payload: PayloadNotification ?= null,

    @SerializedName("type")
    @Expose
    val type: String ?= null
)
