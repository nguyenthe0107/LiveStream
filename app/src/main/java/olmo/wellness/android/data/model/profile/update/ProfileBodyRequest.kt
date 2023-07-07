package olmo.wellness.android.data.model.profile.update

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileBodyRequest(
   @SerializedName("update")
   @Expose
   val updateRequest: ProfileUpdateRequest?= null
)