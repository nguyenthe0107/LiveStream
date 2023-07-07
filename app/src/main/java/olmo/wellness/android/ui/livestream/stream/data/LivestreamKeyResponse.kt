package olmo.wellness.android.ui.livestream.stream.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LivestreamKeyResponse(
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