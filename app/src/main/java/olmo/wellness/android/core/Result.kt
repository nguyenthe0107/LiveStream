package olmo.wellness.android.core

sealed class Result<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Result<T>(data)
    class Error<T>(message: String, data: T? = null) : Result<T>(data, message)
    class Loading<T>(data: T? = null) : Result<T>(data)

    fun onResultReceived(
        onLoading: (() -> Unit)? = null,
        onSuccess: ((result: T?) -> Unit)? = null,
        onError: ((message: String?) -> Unit)? = null
    ){
        when(this){
            is Success -> {
                onSuccess?.invoke(this.data)
            }
            is Loading -> {
                onLoading?.invoke()
            }
            is Error -> {
                onError?.invoke(message)
            }
        }
    }
}