package olmo.wellness.android.ui.livestream.stream.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UpdateLivestreamRequest(
    @Expose
    @SerializedName("status")
    val status: LivestreamStatus ?= null,

    @Expose
    @SerializedName("isRecord")
    val isRecord : Boolean ?= null,

    @Expose
    @SerializedName("userId")
    val userId : Int ?= null,

    @Expose
    @SerializedName("isPin")
    val isPin : Boolean ?= null,
)