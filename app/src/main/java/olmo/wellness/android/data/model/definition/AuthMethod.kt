package olmo.wellness.android.data.model.definition

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

enum class AuthMethod(name: String) {
    @Expose
    @SerializedName("SECRET_KEY")
    SECRET_KEY("SECRET_KEY"),

    @Expose
    @SerializedName("PASSWORD")
    USER_PASS("PASSWORD"),

    @Expose
    @SerializedName("PROVIDER")
    PROVIDER("PROVIDER"),

    @Expose
    @SerializedName("REFRESH_TOKEN")
    REFRESH_TOKEN("REFRESH_TOKEN")
}