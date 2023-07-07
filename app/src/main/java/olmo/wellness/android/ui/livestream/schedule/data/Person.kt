package olmo.wellness.android.ui.livestream.schedule.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Person(
    @Expose
    @SerializedName("id")
    val id: Int,
    @Expose
    @SerializedName("name")
    val name: String
)