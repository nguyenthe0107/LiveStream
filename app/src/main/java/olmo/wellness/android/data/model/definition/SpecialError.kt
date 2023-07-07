package olmo.wellness.android.data.model.definition

enum class SpecialError(val nameError: String) {
    AUTHENTICATION_ERROR("AUTHENTICATION_ERROR"),
    BAD_STATE("BAD_STATE"),
    USER_NOT_FOUND("User not found or verified"),
    USER_NOT_FOUND_V2("User not found"),
    PRINCIPAL_NOT_FOUND("Principal not found"),
    INVALID_CREDENTIALS("Invalid creadentials"),
    USER_REGISTERED("User has already registered"),
    USER_REGISTERED_NOT_VERIFIED("User has been already registered but not verified. Please verify the user again"),
    USER_NOT_VERIFIED("Account had been banned for: User had not been verified");

    companion object {
        fun getByValue(name: SpecialError) = values().find { it.name == name.nameError }
    }
}