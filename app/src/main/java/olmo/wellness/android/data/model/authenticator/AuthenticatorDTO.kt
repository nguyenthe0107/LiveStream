package olmo.wellness.android.data.model.authenticator

import olmo.wellness.android.domain.model.authenticator.Authenticator

data class AuthenticatorDTO(
    val secret: String,
    val qrBase64Img: String,
)

fun AuthenticatorDTO.toAuthenticatorDomain() = Authenticator(secret = secret, qrBase64Img = qrBase64Img)
