package olmo.wellness.android.data.model.report_livestream

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.domain.model.report_livestream.ReportLiveStreamInfo

data class ReportLiveStreamDTO(
    @SerializedName("id")
    @Expose
    val id: Int? = null,

    @SerializedName("createdAt")
    @Expose
    val createdAt: Long? = null,

    @SerializedName("lastModified")
    @Expose
    val lastModified: Long? = null,

    @SerializedName("userId")
    @Expose
    val userId: Int? = null,

    @SerializedName("livestreamId")
    @Expose
    val livestreamId: Int? = null,

    @SerializedName("content")
    @Expose
    val content: String? = null,
)

fun ReportLiveStreamDTO.toReportLiveInfo() : ReportLiveStreamInfo{
    return ReportLiveStreamInfo(id = id, createdAt = createdAt, lastModified = lastModified, userId = userId, livestreamId = livestreamId, content = content)
}
