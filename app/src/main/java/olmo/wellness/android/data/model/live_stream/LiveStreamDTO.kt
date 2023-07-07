package olmo.wellness.android.data.model.live_stream

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.ui.livestream.stream.data.LivestreamKeyResponse

data class LiveStreamDTO (
    @Expose
    @SerializedName("livestreamId")
    val livestreamId: Int ?= null,

    @Expose
    @SerializedName("channel")
    val channel : String,

    @Expose
    @SerializedName("streamKey")
    val streamKey : String,

    @Expose
    @SerializedName("roomChatId")
    val roomChatId : String,
)

fun LiveStreamDTO.toLiveStreamDomain() : LivestreamKeyResponse {
    return LivestreamKeyResponse(livestreamId, channel, streamKey,roomChatId)
}