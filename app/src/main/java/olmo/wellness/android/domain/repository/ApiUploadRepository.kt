package olmo.wellness.android.domain.repository

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import olmo.wellness.android.core.Result
import olmo.wellness.android.domain.model.upload.UploadUrlInfo

interface ApiUploadRepository {
    fun getUploadUrlInfo(mimeType: String): Flow<Result<UploadUrlInfo>>
    fun getMultiUploadUrlInfo(mimeTypes: List<String>) : Flow<Result<List<UploadUrlInfo>>>
    fun uploadFile(presignedUrl: String, fileUri: Uri): Flow<Result<Boolean>>
}