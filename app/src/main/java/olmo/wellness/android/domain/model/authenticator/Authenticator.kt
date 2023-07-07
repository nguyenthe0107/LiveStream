package olmo.wellness.android.domain.model.authenticator

data class Authenticator(
    val secret: String,
    val qrBase64Img: String,
)
