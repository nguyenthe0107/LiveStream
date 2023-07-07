package olmo.wellness.android.data.model.notification

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.domain.model.notification.CountNotificationInfo

data class CountNotificationDTOModel(
    @SerializedName("count")
    @Expose
    val total: Int? = null,
)

fun CountNotificationDTOModel.toNotificationInfoDomain(): CountNotificationInfo {
    return CountNotificationInfo(total = total)
}


