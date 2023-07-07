package olmo.wellness.android.data.model.live_stream

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LiveStreamRequest(
    @SerializedName("title")
    @Expose
    var title : String? = null,
    @SerializedName("hashtag")
    @Expose
    var hashtag : ArrayList<String> ?= null,
    @SerializedName("isPrivate")
    @Expose
    var isPrivate: Boolean ?= null,
    @SerializedName("isSchedule")
    @Expose
    var isSchedule : Boolean ?= null,
    @SerializedName("startTime")
    @Expose
    var startTime : Long ?= null,
    @SerializedName("isRecord")
    @Expose
    var isRecord : Boolean ?= null,
    @SerializedName("thumbnailUrl")
    @Expose
    var thumbnailUrl : String ?= null,
    @SerializedName("isEvent")
    @Expose
    var isEvent : Boolean ?= null,
    @SerializedName("categoryIds")
    @Expose
    var categoryIds : List<Int> ?= null,
    @SerializedName("description")
    @Expose
    var description : String ?= null,

    @SerializedName("serviceIds")
    @Expose
    var serviceIds : List<Int> ?= null,
)
