package olmo.wellness.android.domain.model.request

data class GetProfileRequest(
    val userId: Int?,
    val fields: String?=""
)
