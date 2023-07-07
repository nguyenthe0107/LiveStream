package olmo.wellness.android.data.model

data class PostRequest<T>(
    val records: T
)

data class UpdateRequest<T>(
    val update: T
)

data class ModifiedResponse<T>(
    val modified: T
)

data class ModifiedServiceResponse<T>(
    val services: T
)
