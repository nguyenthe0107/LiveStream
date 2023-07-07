package olmo.wellness.android.data.model.delete_account

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DeleteAccountRequest(
    @SerializedName("reason")
    @Expose
    val reason: String? = null,
)