package olmo.wellness.android.domain.use_case

import android.net.Uri
import olmo.wellness.android.domain.repository.ApiRepository
import olmo.wellness.android.domain.repository.ApiUploadRepository
import javax.inject.Inject
import javax.inject.Provider

class UploadFileUseCase @Inject constructor(
    private val repository: Provider<ApiUploadRepository>
) {

    data class Params(val presignedUrl: String, val fileUri: Uri)

    operator fun invoke(params: Params) = repository.get().uploadFile(params.presignedUrl, params.fileUri)
}