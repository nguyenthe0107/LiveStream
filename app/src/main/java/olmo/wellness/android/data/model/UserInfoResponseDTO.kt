package olmo.wellness.android.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import olmo.wellness.android.domain.model.business.StoreOwner
import olmo.wellness.android.domain.model.login.UserInfoResponse

class UserInfoResponseDTO(
    @Expose
    @SerializedName("userType")
    val userType: String? = null,

    @Expose
    @SerializedName("id")
    val id: Int,

    @Expose
    @SerializedName("principalId")
    val principalId: Int?=null,

    @Expose
    @SerializedName("name")
    val name: String?=null,

    @Expose
    @SerializedName("verified")
    val verified: Boolean?=null,

    @Expose
    @SerializedName("email")
    val email: String?=null,

    @Expose
    @SerializedName("phoneNumber")
    val phoneNumber: String?=null,

    @Expose
    @SerializedName("avatar")
    val avatar: String?=null,

    @Expose
    @SerializedName("coverPhoto")
    val coverPhoto: String?=null,

    @Expose
    @SerializedName("bio")
    val bio: String?=null,

    @Expose
    @SerializedName("store")
    val store: StoreOwner?=null,

    @Expose
    @SerializedName("mfaSecret")
    val mfaSecret: Boolean?=null,

    @Expose
    @SerializedName("useTfa")
    val useTfa: Boolean?=null

)

fun UserInfoResponseDTO.toUserTypeData() =
    UserInfoResponse(id = id, userType = userType, principalId = principalId,
        name = name, verified = verified, email = email,
        phoneNumber = phoneNumber, avatar = avatar,
        coverPhoto = coverPhoto, bio = bio,
        mfaSecret = mfaSecret, useTfa = useTfa, store = store)