package olmo.wellness.android.ui.screen.verification_success

import android.app.Application
import android.net.Uri
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.last
import olmo.wellness.android.R
import olmo.wellness.android.core.BaseViewModel
import olmo.wellness.android.domain.use_case.UploadFileUseCase
import javax.inject.Inject

@HiltViewModel
class VerificationStep1ViewModel @Inject constructor(
    application: Application,
    private val uploadFileUseCase: UploadFileUseCase
) : BaseViewModel(application) {

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun setLoadingState(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    suspend fun uploadCode(code: String): String? {
        var uploadSuccess: String? = null
        try {
            _error.value = ""

        } catch (e: Exception) {
            _error.value = context.getString(R.string.upload_fail)
        }
        return uploadSuccess
    }
}
