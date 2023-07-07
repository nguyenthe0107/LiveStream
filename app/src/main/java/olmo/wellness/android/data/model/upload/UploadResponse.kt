package olmo.wellness.android.data.model.upload

import olmo.wellness.android.domain.model.upload.UploadUrlInfo

data class UploadResponse(
    val objectKey: String,
    val putPresignedUrl: String?,
    val deletePresignedUrl: String,
)

fun UploadResponse.toUploadUrl() = UploadUrlInfo(
    objectKey = objectKey,
    putPresignedUrl = putPresignedUrl,
    deletePresignedUrl = deletePresignedUrl
)