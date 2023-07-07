package olmo.wellness.android.data.model.report_livestream

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReportLiveStreamRequestDTO(
    @SerializedName("livestreamId")
    @Expose
    val livestreamId: Int ?= null,

    @SerializedName("content")
    @Expose
    val content: String ?= null,
)

