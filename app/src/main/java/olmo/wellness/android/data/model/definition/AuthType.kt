package olmo.wellness.android.data.model.definition

enum class AuthType(name: String) {
    FIREBASE("FIREBASE"),
    TIKTOK("TIKTOK"),
    USER_PASS("USER_PASS"),
    PROVIDER("PROVIDER"),
    REFRESH_TOKEN("REFRESH_TOKEN"),
    SECRET_KEY("SECRET_KEY")
}