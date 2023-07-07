package olmo.wellness.android.data.model.fcm

data class AppUserRequest(
    val userId : Int,
    val os: String = "Android",
    val deviceId: String ,
    val firebaseToken: String
)
