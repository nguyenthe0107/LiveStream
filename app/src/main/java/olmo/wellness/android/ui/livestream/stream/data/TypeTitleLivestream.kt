package olmo.wellness.android.ui.livestream.stream.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

enum class TypeTitleLivestream(name: String) {
    //LiveNow | Upcoming | Event | TopTrending | Recommended | SweeplistVideo | SweeplistLivestream | FinishedOfSeller
    @Expose
    @SerializedName("LiveNow")
    LiveNow("LiveNow"),

    @Expose
    @SerializedName("Upcoming")
    Upcoming("Upcoming"),

    @Expose
    @SerializedName("Event")
    Event("Event"),

    @Expose
    @SerializedName("TopTrending")
    TopTrending("TopTrending"),

    @Expose
    @SerializedName("Recommended")
    Recommended("Recommended"),

    @Expose
    @SerializedName("SweeplistVideo")
    SweeplistVideo("SweeplistVideo"),

    @Expose
    @SerializedName("SweeplistLivestream")
    SweeplistLivestream("SweeplistLivestream"),

    @Expose
    @SerializedName("FinishedOfSeller")
    FinishedOfSeller("FinishedOfSeller"),



}