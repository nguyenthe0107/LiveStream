package olmo.wellness.android.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.data.model.definition.AuthMethod
import olmo.wellness.android.data.model.definition.UserTypeModel
import olmo.wellness.android.ui.screen.navigation.ScreenName

class RegisterRequest(

    @Expose
    @SerializedName("userType")
    val userType: UserTypeModel,

    @Expose
    @SerializedName("subCategoryIds")
    val subCategoryIds: List<Int> = mutableListOf(),

    @Expose
    @SerializedName("authMethod")
    val authMethod : AuthMethod ?= null,

    @Expose
    @SerializedName("authData")
    val authData : ArrayList<String> = arrayListOf()
)