package olmo.wellness.android.ui.livestream.stream.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

enum class LivestreamStatus(name: String) {
    @Expose
    @SerializedName("ON_LIVE")
    ON_LIVE("ON_LIVE"),

    @Expose
    @SerializedName("PAUSED")
    PAUSED("PAUSED"),

    @Expose
    @SerializedName("FINISHED")
    FINISHED("FINISHED"),

    @Expose
    @SerializedName("UPCOMING")
    UPCOMING("UPCOMING"),

    @Expose
    @SerializedName("LIVESTREAM")
    LIVESTREAM("LIVESTREAM"),

    @Expose
    @SerializedName("VIDEO")
    VIDEO("VIDEO"),
}