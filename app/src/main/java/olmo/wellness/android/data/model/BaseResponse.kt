package olmo.wellness.android.data.model

data class BaseResponse<T>(
    val total: Int,
    val records: List<T>
)