package olmo.wellness.android.data.model.session

data class SessionRequest(
    val authMethod: String = "REFRESH_TOKEN",
    val authData: List<String>
)
