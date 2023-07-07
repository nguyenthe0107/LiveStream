package olmo.wellness.android.domain.model.notification

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.data.model.notification.DataNotification

data class NotificationInfo(
    @SerializedName("createdAt")
    @Expose
    val createdAt: Long? = null,

    @SerializedName("data")
    @Expose
    val dataNotification : DataNotification? = null,

    @SerializedName("description")
    @Expose
    val description: String? = null,

    @SerializedName("groupTopics")
    @Expose
    val groupTopics: List<Any?>? = null,

    @SerializedName("_id")
    @Expose
    val id: String? = null,

    @SerializedName("seens")
    @Expose
    val seens: List<Any?>? = null,

    @SerializedName("title")
    @Expose
    val title: String? = null,

    @SerializedName("tos")
    @Expose
    val tos: List<Int?>? = null,

    @SerializedName("type")
    @Expose
    val type: String? = null,

    @SerializedName("updatedAt")
    @Expose
    val updatedAt: String? = null,

    @SerializedName("isSeen")
    @Expose
    val isSeen: Boolean? = null,

    var isRecently : Boolean ?= false,

    @SerializedName("image")
    @Expose
    val image: String ?= null

){
    fun update(isRecently: Boolean?) = NotificationInfo(createdAt = createdAt,
        dataNotification = dataNotification, description = description, groupTopics = groupTopics,
        id = id, seens = seens, title = title, tos = tos, type = type, updatedAt = updatedAt,
        isSeen = isSeen, isRecently = isRecently, image = image
    )
}

