package olmo.wellness.android.data.model.chat


import com.google.gson.annotations.SerializedName

data class FileObject(
    @SerializedName("type")
    val type: String?,
    @SerializedName("url")
    val url: String?
)