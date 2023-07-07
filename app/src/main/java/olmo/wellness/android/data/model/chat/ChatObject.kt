package olmo.wellness.android.data.model.chat


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChatObject(
    @SerializedName("coin")
    @Expose
    val coin: Int?=null,
    @SerializedName("id")
    @Expose
    val id: Int?=null,
    @SerializedName("image")
    @Expose
    val image: String?=null,
    @SerializedName("name")
    @Expose
    val name: String?=null,
    @SerializedName("files")
    @Expose
    val files :List<FileObject?>?=null,
)