package olmo.wellness.android.extension

import arrow.core.Either
import olmo.wellness.android.webrtc.network.NetworkHandler

fun <T> NetworkHandler.withAvailableNetwork(value: T) = when (isNetworkAvailable) {
    true -> Either.Right(value)
    false -> Either.Left(AppExceptions.NetworkException)
}