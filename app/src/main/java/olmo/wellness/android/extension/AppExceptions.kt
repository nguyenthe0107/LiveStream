package olmo.wellness.android.extension

object AppExceptions {

    object Messages {
        const val DEFAULT = "Something get wrong."
        const val NETWORK = "Connection unavailable."
        const val PERMISSION = "Permissions unavailable."
    }

    object NetworkException : Exception(Messages.NETWORK)
    object PermissionException : Exception(Messages.PERMISSION)

}