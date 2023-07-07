package olmo.wellness.android.data.model.upload

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UploadFilesResponse (
    @Expose
    @SerializedName("fullPath")
    val fullPath : List<String>?=null
)