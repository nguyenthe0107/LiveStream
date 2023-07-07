package olmo.wellness.android.domain.model.livestream

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.MutableStateFlow
import olmo.wellness.android.data.model.chat.room.DetailRoom
import olmo.wellness.android.data.model.chat.room.User
import olmo.wellness.android.ui.livestream.stream.data.TypeTitleLivestream
import java.io.Serializable

data class LiveSteamShortInfo(
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
    var isFollow: Boolean? = false,

    val fromProfile : Boolean = false,
    val isStreaming: Boolean? = false,
    val isLiveStream: Boolean? = false,
    val typeTitle: TypeTitleLivestream = TypeTitleLivestream.SweeplistVideo,
    val isShowIconReminder: Boolean ?= false,
    var transformFollow: Boolean ?= false,
    val fromSearch : Boolean = false,

    ): Serializable{
    fun copy(isFollow: Boolean?): LiveSteamShortInfo {
        return LiveSteamShortInfo(
            id = id,
            isFollow = isFollow,
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
            userId = userId,
            user = user,
            viewCount = viewCount,
            recordUrl = recordUrl,
            playbackUrl = playbackUrl,
            isStreaming = isStreaming,
            isLiveStream = isLiveStream,
            typeTitle = typeTitle,
            fromProfile = fromProfile,
            transformFollow = transformFollow
        )
    }
}