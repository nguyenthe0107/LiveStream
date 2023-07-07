package olmo.wellness.android.domain.model.verification1.response

data class Verification(
    val id : Int?,
    val createdAt : Number?,
    val lastModified : Number?,
    val businessId : Int?,
    val staffUserId: Int?,
    val sivStep: Int?,
    val message : String?,
    val status : String?
)
