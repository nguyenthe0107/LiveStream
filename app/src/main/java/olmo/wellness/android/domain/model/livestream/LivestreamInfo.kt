package olmo.wellness.android.domain.model.livestream

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.data.model.chat.room.DetailRoom
import olmo.wellness.android.data.model.chat.room.User
import olmo.wellness.android.domain.model.category.Category
import olmo.wellness.android.domain.model.category.CategoryResponse
import java.io.Serializable

data class LivestreamInfo(
    @Expose
    @SerializedName("id")
    val id: Int? = null,

    @Expose
    @SerializedName("recordUrl")
    val recordUrl: String? = "",

    @Expose
    @SerializedName("playbackUrl")
    val playbackUrl: String? = "",

    @Expose
    @SerializedName("userId")
    val userId: Int? = null,

    @Expose
    @SerializedName("isRecord")
    val isRecord: Boolean = false,

    @Expose
    @SerializedName("channel")
    val channel: String? = "",

    @Expose
    @SerializedName("streamKey")
    val streamKey: String? = "",

    @Expose
    @SerializedName("isPrivate")
    val isPrivate: Boolean? = false,

    @Expose
    @SerializedName("status")
    val status: String? = "",

    @Expose
    @SerializedName("createdAt")
    var createdAt: Long? = null,

    @Expose
    @SerializedName("lastModified")
    val lastModified: Long? = null,

    @Expose
    @SerializedName("startTime")
    var startTime: Long? = null,

    @Expose
    @SerializedName("endTime")
    val endTime: Long? = null,

    @Expose
    @SerializedName("channelName")
    val channelName: String? = null,

    @Expose
    @SerializedName("thumbnailUrl")
    val thumbnailUrl: String? = null,

    @Expose
    @SerializedName("title")
    var title: String? = null,

    @Expose
    @SerializedName("hashtag")
    var hashtag: String? = null,

    @SerializedName("roomChat")
    val roomChat: DetailRoom? = null,

    @SerializedName("roomChatId")
    val roomChatId: String? = null,

    @SerializedName("heartCount")
    val heartCount: Int? = null,

    @SerializedName("maxViewCount")
    val maxViewCount: Int? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("user")
    val user: User? = null,

    @SerializedName("viewCount")
    val viewCount: Int? = null,

    @SerializedName("avatar")
    val avatar : String ?= null,

    val isLiveStream: Boolean? = false,
    val isComing: Boolean? = false,
    val isStreaming: Boolean? = false,
    val totalUser: Int ?= 0,
    val avatarProfile : String ?= "",
    val fromProfile : Boolean = false,

    @Expose
    @SerializedName("isFollow")
    var isFollow: Boolean ?= false,

    @Expose
    @SerializedName("categories")
    val categories: List<Category> ?= emptyList()

) : Serializable

fun LivestreamInfo.toLiveShortItem(): LiveSteamShortInfo{
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
        fromProfile = fromProfile
    )
}