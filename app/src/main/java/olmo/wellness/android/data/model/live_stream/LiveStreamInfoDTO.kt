package olmo.wellness.android.data.model.live_stream

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.domain.model.category.Category
import olmo.wellness.android.domain.model.livestream.LivestreamInfo

data class LiveStreamInfoDTO (
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
    val createdAt: Long? = null,

    @Expose
    @SerializedName("lastModified")
    val lastModified: Long? = null,

    @Expose
    @SerializedName("startTime")
    val startTime: Long? = null,

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
    val title: String? = null,

    @Expose
    @SerializedName("hashtag")
    val hashtag: String? = null,

    @Expose
    @SerializedName("categories")
    val categories: List<Category> ?= emptyList()
)

fun LiveStreamInfoDTO.toLiveStreamInfoDomain() : LivestreamInfo{
    return LivestreamInfo(
        id, recordUrl, playbackUrl, userId, isRecord, channel, streamKey, isPrivate, status,
        createdAt, lastModified, startTime, endTime, channelName, thumbnailUrl, title, hashtag,
        categories = categories
    )
}