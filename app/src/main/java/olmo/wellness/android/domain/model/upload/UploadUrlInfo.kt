package olmo.wellness.android.domain.model.upload

data class UploadUrlInfo(
    val objectKey: String,
    val putPresignedUrl: String?,
    val deletePresignedUrl: String,
)
