package olmo.wellness.android.ui.screen.profile_setting_screen.verification_step_1

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.last
import olmo.wellness.android.R
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.core.Constants.MIME_IMAGE
import olmo.wellness.android.domain.use_case.GetUploadUrlInfoUseCase
import olmo.wellness.android.domain.use_case.GetUploadUrlInfoUseCase.Params
import olmo.wellness.android.domain.use_case.UploadFileUseCase
import javax.inject.Inject

@HiltViewModel
open class UploadFileViewModel @Inject constructor(
    application: Application,
    private val getUploadUrlInfoUseCase: GetUploadUrlInfoUseCase,
    private val uploadFileUseCase: UploadFileUseCase
) : BaseViewModel(application) {

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun setLoadingState(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    suspend fun uploadFile(fileUri: Uri): String? {
        var uploadSuccess: String? = null
        try {
            _error.value = ""
            val mimeType = context.contentResolver.getType(fileUri) ?: "image/$MIME_IMAGE"

            val uploadInfo = viewModelScope.async {
                getUploadUrlInfoUseCase(
                    Params(mimeType.substring(mimeType.indexOf("/") + 1).ifEmpty { "" })
                ).last()
            }
            uploadInfo.await().data?.let {
                val uploadStatus = viewModelScope.async {
                    requestUploadFile(
                        it.putPresignedUrl.orEmpty(),
                        fileUri
                    ).last()
                }
                val result = uploadStatus.await().data ?: false
                if (result) {
                    uploadSuccess = it.objectKey
                } else _error.value = context.getString(R.string.upload_fail)
            }
        } catch (e: Exception) {
            _error.value = context.getString(R.string.upload_fail)
        }
        return uploadSuccess
    }

    private fun requestUploadFile(presignedUrl: String, fileUri: Uri) =
        uploadFileUseCase(UploadFileUseCase.Params(presignedUrl, fileUri))

}
