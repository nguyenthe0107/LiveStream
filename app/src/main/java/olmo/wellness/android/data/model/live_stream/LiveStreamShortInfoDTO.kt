package olmo.wellness.android.data.model.live_stream

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.data.model.chat.room.DetailRoom
import olmo.wellness.android.data.model.chat.room.User
import olmo.wellness.android.domain.model.livestream.LiveSteamShortInfo
import java.io.Serializable

data class LiveStreamShortInfoDTO(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("createdAt")
    val createdAt: Long? = null,

    @SerializedName("heartCount")
    val heartCount: Int? = null,

    @SerializedName("maxViewCount")
    val maxViewCount: Int? = null,

    @SerializedName("roomChat")
    val roomChat: DetailRoom? = null,

    @SerializedName("roomChatId")
    val roomChatId: String? = null,

    @SerializedName("startTime")
    val startTime: Long? = null,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("thumbnailUrl")
    val thumbnailUrl: String? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("user")
    val user: User? = null,

    @SerializedName("userId")
    val userId: Int? = null,

    @SerializedName("viewCount")
    val viewCount: Int? = null,

    @Expose
    @SerializedName("recordUrl")
    val recordUrl: String? = "",

    @Expose
    @SerializedName("playbackUrl")
    val playbackUrl: String? = "",

    @Expose
    @SerializedName("isFollow")
    val isFollow: Boolean? = false,

) : Serializable

fun LiveStreamShortInfoDTO.toLiveSteamShortInfo(): LiveSteamShortInfo{
    return LiveSteamShortInfo(id = id,
        createdAt = createdAt,
        heartCount = heartCount,
        maxViewCount = maxViewCount,
        roomChat = roomChat,
        roomChatId = roomChatId,
        startTime = startTime,
        status = status,
        description = description,
        thumbnailUrl = thumbnailUrl,
        title = title,
        user = user,
        userId = userId,
        viewCount = viewCount,
        playbackUrl = playbackUrl,
        recordUrl = recordUrl,
        isFollow = isFollow
    )
}