package olmo.wellness.android.data.model.upload

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.data.model.definition.AuthMethod

data class UploadFilesRequest(
    @Expose
    @SerializedName("tmpUris")
    val tmpUris: List<String>? = null,
    @Expose
    @SerializedName("roomChatId")
    val roomChatId: String? = null,
)