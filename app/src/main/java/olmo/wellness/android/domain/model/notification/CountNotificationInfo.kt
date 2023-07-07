package olmo.wellness.android.domain.model.notification

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.data.model.notification.DataNotification

data class CountNotificationInfo(
    @SerializedName("count")
    @Expose
    val total: Int? = null,
)
